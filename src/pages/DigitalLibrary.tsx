import { useState } from 'react';
import { useBookStore } from '../store/useBookStore';
import { useNotificationStore } from '../store/useNotificationStore';
import { useNavigate } from 'react-router-dom';
import EmptyState from '../components/EmptyState';
import ModalComponent from '../components/ModalComponent';
import {
  Library,
  BookOpen,
  ArrowRight,
  Download,
  CheckCircle,
  Play,
  RotateCcw,
  BookMarked,
  Sparkles,
  ChevronRight,
  Maximize,
  Grid3X3,
  ListFilter,
  Flame,
} from 'lucide-react';
import RatingComponent from '../components/RatingComponent';

export default function DigitalLibrary() {
  const navigate = useNavigate();
  const showToast = useNotificationStore();
  const { books, library, updateReadingProgress, downloadFormat } = useBookStore();
  
  // Loader States for simulate downloads
  const [downloadingBookId, setDownloadingBookId] = useState<string | null>(null);
  const [downloadProgress, setDownloadProgress] = useState(0);

  // Reader Modal States
  const [readerBookId, setReaderBookId] = useState<string | null>(null);
  const [readerCurrentPage, setReaderCurrentPage] = useState(1);
  const [fontSize, setFontSize] = useState<'sm' | 'base' | 'lg' | 'xl'>('base');

  const activeDownloads = (bId: string) => {
    setDownloadingBookId(bId);
    setDownloadProgress(0);
    
    // Simulate active download speeds
    const interval = setInterval(() => {
      setDownloadProgress((prev) => {
        if (prev >= 100) {
          clearInterval(interval);
          setDownloadingBookId(null);
          downloadFormat(bId);
          showToast.showToast('Livro baixado com sucesso para o cache offline do dispositivo!', 'success');
          return 100;
        }
        return prev + 20; // 20% increases
      });
    }, 280);
  };

  const handleOpenReader = (bId: string, currentProgress: number) => {
    const bookDetails = books.find((b) => b.id === bId);
    if (!bookDetails) return;

    setReaderBookId(bId);
    // Rough estimate of page number matching progress
    const totalPages = bookDetails.pages || 300;
    const estPage = Math.max(1, Math.round((currentProgress / 100) * totalPages));
    setReaderCurrentPage(estPage);
  };

  const handleReaderPageChange = (change: number, totalPages: number) => {
    const nextPage = Math.max(1, Math.min(totalPages, readerCurrentPage + change));
    setReaderCurrentPage(nextPage);

    // Sync newly adjusted progress directly under Zustand store
    const calculatedProgress = Math.round((nextPage / totalPages) * 100);
    if (readerBookId) {
      updateReadingProgress(readerBookId, calculatedProgress);
    }
  };

  const handleProgressSliderChange = (bId: string, value: number) => {
    updateReadingProgress(bId, value);
  };

  if (library.length === 0) {
    return (
      <div className="py-20 max-w-lg mx-auto">
        <EmptyState
          title="Biblioteca Digital Vazia"
          description="Adquira e-books digitais nos formatos PDF ou ePUB em nosso catálogo para preencher sua estante digital instantaneamente."
          icon={Library}
          actionLabel="Girar para o Catálogo"
          onAction={() => navigate('/catalog')}
        />
      </div>
    );
  }

  // Find continues reading item (item last modified, default: first index)
  const continueReadingItem = library[0];
  const continueBook = books.find((b) => b.id === continueReadingItem?.bookId);

  // Font sizing styles dictionary
  const fontSizes = {
    sm: 'text-xs leading-relaxed',
    base: 'text-sm sm:text-base leading-relaxed',
    lg: 'text-base sm:text-lg leading-relaxed',
    xl: 'text-lg sm:text-xl leading-relaxed',
  };

  return (
    <div className="py-6 md:py-10 space-y-8" id="bookverse-digitallibrary">
      
      {/* HEADER ROW */}
      <div className="border-b border-zinc-150 pb-5 dark:border-zinc-800 flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <span className="text-xs font-extrabold uppercase tracking-widest text-blue-606 dark:text-blue-400 font-mono font-bold">Meu Espaço Digital</span>
          <h1 className="text-3xl font-bold text-zinc-909 dark:text-zinc-50 mt-1">Minha Biblioteca Digital</h1>
          <p className="text-sm text-zinc-550 mt-1">Continue lendo e-books salvos em formato ePUB ou consulte PDFs.</p>
        </div>

        {/* Reading Streak Indicator */}
        <div className="flex items-center gap-2 rounded-2xl bg-amber-500/10 px-4 py-2 border border-amber-500/20 self-start sm:self-auto text-amber-600 dark:text-amber-400">
          <Flame className="h-5 w-5 animate-pulse" />
          <div className="text-left">
            <span className="text-[9px] font-mono uppercase tracking-wider block leading-none">Ofensiva de Leitura</span>
            <span className="text-xs font-bold">12 Dias Consecutivos</span>
          </div>
        </div>
      </div>

      {/* 2. CONTINUE READING PANEL SECTOR */}
      {continueBook && (
        <section className="rounded-3xl border border-blue-100 bg-blue-50/20 dark:border-blue-900/40 p-6 md:p-8 grid grid-cols-1 gap-6 md:grid-cols-12 md:items-center" id="panel-continue-reading">
          <div className="md:col-span-8 space-y-4">
            <span className="inline-flex items-center gap-1.5 rounded-full bg-blue-600/15 px-3 py-1 text-xs font-bold text-blue-606 dark:text-blue-400 uppercase tracking-widest font-mono">
              <Flame className="h-3 w-3 animate-spin-slow" /> Continuar Lendo Sessão Recente
            </span>
            <h2 className="text-2xl font-black text-zinc-900 dark:text-zinc-50 font-sans leading-tight">
              {continueBook.title}
            </h2>
            <p className="text-xs text-zinc-500">por {continueBook.publisher || 'Editora Mulemba'}</p>
            
            {/* Range value sliders progress */}
            <div className="space-y-2 max-w-md">
              <div className="flex justify-between text-xs font-mono font-bold text-zinc-650 dark:text-zinc-400 font-bold">
                <span>Progresso de Leitura</span>
                <span>{continueReadingItem.progress}% lido</span>
              </div>
              <input
                type="range"
                min="0"
                max="100"
                value={continueReadingItem.progress}
                onChange={(e) => handleProgressSliderChange(continueBook.id, parseInt(e.target.value))}
                className="w-full h-2 bg-blue-100 rounded-lg appearance-none cursor-pointer dark:bg-zinc-800 accent-blue-600"
              />
              <span className="text-[10px] text-zinc-400 block font-mono italic">O progresso é sincronizado de forma automática com nossos servidores.</span>
            </div>

            <div className="pt-2 flex flex-wrap gap-2.5">
              <button
                onClick={() => handleOpenReader(continueBook.id, continueReadingItem.progress)}
                className="inline-flex items-center justify-center gap-1.5 rounded-xl bg-blue-650 hover:bg-blue-750 text-white font-bold text-xs px-5 py-2.5 shadow cursor-pointer transition select-none"
              >
                <Play className="h-4 w-4 fill-current" /> Abrir Leitor Digital
              </button>
              
              {!continueReadingItem.downloaded ? (
                <button
                  type="button"
                  onClick={() => activeDownloads(continueBook.id)}
                  disabled={downloadingBookId === continueBook.id}
                  className="rounded-xl border border-zinc-250 hover:bg-zinc-100 px-5 py-2.5 text-xs font-bold inline-flex items-center justify-center gap-1.5 text-zinc-850 cursor-pointer dark:border-zinc-800 dark:bg-zinc-900"
                >
                  {downloadingBookId === continueBook.id ? (
                    <span className="animate-pulse">Baixando {downloadProgress}%</span>
                  ) : (
                    <><Download className="h-4 w-4 text-emerald-555" /> Salvar Offline</>
                  )}
                </button>
              ) : (
                <span className="inline-flex items-center gap-1.5 text-emerald-600 bg-emerald-50 dark:bg-emerald-950/20 border border-emerald-200 dark:border-emerald-900/40 rounded-xl px-4 py-2.5 text-xs font-bold leading-none select-none">
                  <CheckCircle className="h-4 w-4" /> Disponível Offline
                </span>
              )}
            </div>
          </div>

          {/* Floated Graphic layout */}
          <div className="md:col-span-4 flex justify-center md:justify-end">
            <div className={`h-40 w-28.5 rounded-2xl bg-gradient-to-br ${continueBook.coverColor} relative overflow-hidden flex flex-col justify-between p-4 text-white shadow-xl border border-white/5 font-serif`}>
              <div className="absolute top-0 bottom-0 left-0 w-2 bg-black/10" />
              <div className="absolute top-0 bottom-0 left-2 w-0.5 bg-white/5" />
              <span className="text-[8px] font-mono text-zinc-300 uppercase self-center tracking-widest text-center truncate w-full">Capa do Livro</span>
              <span className="font-bold text-xs text-center leading-tight line-clamp-3 my-auto">{continueBook.title}</span>
              <span className="text-[8px] font-mono text-zinc-400 self-center">PROGRESSO</span>
            </div>
          </div>
        </section>
      )}
         {/* 3. GRID OF DIGITAL SHELVES */}
      <section className="space-y-6">
        <h3 className="text-lg font-bold text-zinc-900 dark:text-zinc-50 flex items-center gap-2">
          <BookMarked className="h-5 w-5 text-blue-500" /> Minhas Obras Digitais ({library.length})
        </h3>

        <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 md:grid-cols-3">
          {library.map((item) => {
            const b = books.find((x) => x.id === item.bookId);
            if (!b) return null;
            return (
              <div
                key={item.bookId}
                className="rounded-2xl border border-zinc-150 p-5 bg-white dark:bg-zinc-900 dark:border-zinc-805 space-y-4"
              >
                <div className="flex items-start gap-4 justify-between">
                  <div className="flex items-center gap-3 overflow-hidden flex-1 select-none">
                    <div className={`h-14 w-10.5 rounded bg-gradient-to-br ${b.coverColor} relative overflow-hidden flex items-center justify-center text-white font-serif font-bold text-[7px] shadow-sm`}>
                      <div className="absolute top-0 bottom-0 left-0 w-0.5 bg-white/5" />
                      COV
                    </div>
                    <div className="overflow-hidden">
                      <h4 className="font-bold text-xs text-zinc-901 dark:text-zinc-50 truncate">{b.title}</h4>
                      <span className="text-[10px] text-zinc-440 font-mono block mt-0.5 uppercase tracking-wide font-semibold">{item.format.toUpperCase()} Padrão</span>
                    </div>
                  </div>
                  {item.downloaded ? (
                    <CheckCircle className="h-5 w-5 text-emerald-500 flex-shrink-0" />
                  ) : (
                    <button
                      onClick={() => activeDownloads(b.id)}
                      disabled={downloadingBookId === b.id}
                      className="rounded-xl p-1.5 border hover:bg-zinc-50 flex-shrink-0 text-zinc-400 hover:text-blue-600 dark:border-zinc-800"
                    >
                      <Download className="h-4 w-4" />
                    </button>
                  )}
                </div>

                {/* Progress Indicators */}
                <div className="space-y-1.5 pt-2">
                  <div className="flex justify-between items-center text-[10px] font-bold font-mono text-zinc-451">
                    <span>Progresso de Leitura</span>
                    <span>{item.progress}%</span>
                  </div>
                  <div className="w-full bg-zinc-100 h-1 rounded-full dark:bg-zinc-800 overflow-hidden">
                    <div className="bg-blue-650 h-full rounded-full" style={{ width: `${item.progress}%` }} />
                  </div>
                  <input
                    type="range"
                    min="0"
                    max="100"
                    value={item.progress}
                    onChange={(e) => handleProgressSliderChange(b.id, parseInt(e.target.value))}
                    className="w-full h-1 h-1.5 cursor-pointer accent-blue-600 bg-transparent shrink-0 opacity-0 group-hover:opacity-100 transition"
                  />
                </div>

                {/* Open interaction */}
                <button
                  onClick={() => handleOpenReader(b.id, item.progress)}
                  className="w-full flex items-center justify-center gap-1.5 rounded-xl bg-zinc-50 border border-zinc-150 py-2 text-xs font-bold text-zinc-705 dark:bg-zinc-850 dark:border-zinc-800 dark:text-zinc-300 hover:bg-blue-600 hover:text-white transition cursor-pointer select-none"
                >
                  <Play className="h-3 w-3 fill-current" /> Abrir no Leitor
                </button>
              </div>
            );
          })}
        </div>
      </section>

      {/* 4. HISTORIC LOG Reading log timeline */}
      <section className="rounded-3xl border border-zinc-150 p-6 bg-zinc-50/50 dark:bg-zinc-950/20 dark:border-zinc-805 space-y-4">
        <h3 className="text-base font-bold text-zinc-904 dark:text-zinc-50">Histórico de Leitura do Aparelho</h3>
        
        <div className="space-y-4">
          <div className="flex items-start gap-3 text-xs border-b pb-3 border-zinc-100 dark:border-zinc-805">
            <span className="font-mono text-[10px] bg-blue-100 text-blue-700 font-bold px-2 py-0.5 rounded uppercase leading-none mt-0.5">Hoje</span>
            <p className="text-zinc-650 dark:text-zinc-350">
              Leu <strong>15 páginas</strong> de <strong>Mindfulness & Reconfiguração Mental</strong>. Foco atingido: 42 minutos.
            </p>
          </div>
          <div className="flex items-start gap-3 text-xs border-b pb-3 border-zinc-100 dark:border-zinc-805">
            <span className="font-mono text-[10px] bg-zinc-200 text-zinc-700 font-bold px-2 py-0.5 rounded uppercase leading-none mt-0.5">Ontem</span>
            <p className="text-zinc-650 dark:text-zinc-350">
              Leu <strong>28 páginas</strong> do livro <strong>Sinapse Cognitiva & Algoritmos</strong>. Cache carregado localmente.
            </p>
          </div>
          <div className="flex items-start gap-3 text-xs pb-1">
            <span className="font-mono text-[10px] bg-zinc-200 text-zinc-700 font-bold px-2 py-0.5 rounded uppercase leading-none mt-0.5">28 de Maio</span>
            <p className="text-zinc-650 dark:text-zinc-350">
              Sincronização automática das anotações salvas em seus dispositivos para backup geral em Cloud.
            </p>
          </div>
        </div>
      </section>

      {/* 5. GIGANTIC VIRTUAL FLUID EBOOK READER MODAL! */}
      {readerBookId && (
        <ModalComponent
          isOpen={!!readerBookId}
          onClose={() => setReaderBookId(null)}
          title={`Leitor Digital Virtual: "${books.find((b) => b.id === readerBookId)?.title}"`}
          size="full"
        >
          {(() => {
            const readerBook = books.find((x) => x.id === readerBookId);
            const totalPages = readerBook?.pages || 350;
            return (
              <div className="flex flex-col h-full justify-between" id="virtual-epub-reader-body">
                {/* Control bar */}
                <div className="flex items-center justify-between border-b pb-3 gap-4 mb-4 text-xs font-mono font-bold text-zinc-500">
                  <div className="flex items-center gap-1.5 bg-zinc-100 p-1 rounded-lg dark:bg-zinc-800">
                    <button
                      type="button"
                      onClick={() => setFontSize('sm')}
                      className={`px-2 py-1.5 rounded ${fontSize === 'sm' ? 'bg-white text-zinc-900 shadow-sm' : 'hover:bg-white/40'}`}
                    >
                      A-
                    </button>
                    <button
                      type="button"
                      onClick={() => setFontSize('base')}
                      className={`px-2 py-1.5 rounded ${fontSize === 'base' ? 'bg-white text-zinc-909 shadow-sm' : 'hover:bg-white/40'}`}
                    >
                      Padrão A
                    </button>
                    <button
                      type="button"
                      onClick={() => setFontSize('lg')}
                      className={`px-2 py-1.5 rounded ${fontSize === 'lg' ? 'bg-white text-zinc-900 shadow-sm' : 'hover:bg-white/40'}`}
                    >
                      A+
                    </button>
                    <button
                      type="button"
                      onClick={() => setFontSize('xl')}
                      className={`px-2 py-1.5 rounded ${fontSize === 'xl' ? 'bg-white text-zinc-900 shadow-sm' : 'hover:bg-white/40'}`}
                    >
                      A++
                    </button>
                  </div>
                  <div>
                    Página <span className="text-blue-600">{readerCurrentPage}</span> de {totalPages}
                  </div>
                </div>

                {/* Reader passage panel */}
                <div className={`flex-1 overflow-y-auto font-serif text-zinc-800 dark:text-zinc-200 p-4 max-w-2xl mx-auto space-y-4 antialiased leading-relaxed ${fontSizes[fontSize]}`}>
                  <p className="font-sans font-bold text-xs uppercase text-zinc-400 tracking-widest text-center border-b pb-1">
                    Livraria Mulemba • Leitor Interativo Virtual de E-books
                  </p>
                  
                  {/* render dummy passages depending on even or odd pages */}
                  {readerCurrentPage % 2 === 1 ? (
                    <>
                      <h3 className="font-sans text-xl font-bold tracking-tight text-zinc-900 dark:text-white pt-2">Capítulo {Math.ceil(readerCurrentPage / 30)}: Alinhamento de Contexto</h3>
                      <p className="indent-8">
                        Os parâmetros neurais se alinharam perfeitamente. Era, afinal, a sequência que Raymond Vance previra durante a fase de análise dos manuais de criptologia. Os blocos de memória mantinham partições uniformes de pacotes criptografados.
                      </p>
                      <p>
                        "Deixe as redes neurais convergirem", comentou Arthur, pressionando as teclas do terminal. A tela piscou em verde e uma série de modelos matemáticos se projetou pela tela, revelando as camadas modulares se combinando de forma harmônica.
                      </p>
                      <p>
                        Ele percebeu que a resiliência cognitiva não consistia apenas em rotinas puramente biológicas. Era sobre a construção cuidadosa de metas intelectuais pacíficas dispostas a catalisar pensamentos brilhantes.
                      </p>
                    </>
                  ) : (
                    <>
                      <h3 className="font-sans text-xl font-bold tracking-tight text-zinc-900 pt-2 dark:text-white">Capítulo {Math.ceil(readerCurrentPage / 30)}: A Expansão Macroeconômica</h3>
                      <p className="indent-8">
                        As automações de índices representavam parâmetros singulares de crescimento contínuo. Ao concentrar a alocação de fundos em índices de menor escala, o portfólio mantinha estruturas de proteção otimizadas para contrariar oscilações inflacionárias de curto prazo.
                      </p>
                      <p>
                        Marcus Sterling Jr. ajustou as métricas em seu livro-razão pessoal. Os números progrediam de forma sustentável, imunes às instabilidades do mercado global, garantindo que os vetores de crescimento continuassem equilibrando os riscos de forma perfeitamente autônoma.
                      </p>
                      <p>
                        "O mecanismo permanece em perfeita estabilidade", documentou ele com satisfação. Os algoritmos eram simples, sólidos e prontos para embasar as novas diretrizes recomendadas.
                      </p>
                    </>
                  )}
                </div>

                {/* Footer pagers */}
                <div className="flex items-center justify-between border-t pt-4 mt-6">
                  <button
                    onClick={() => handleReaderPageChange(-1, totalPages)}
                    disabled={readerCurrentPage === 1}
                    className="rounded-xl border border-zinc-250 py-2 px-4 text-xs font-bold text-zinc-700 bg-white dark:border-zinc-810 dark:bg-zinc-900 dark:text-zinc-350 hover:bg-zinc-50 disabled:opacity-40"
                  >
                    Página Anterior
                  </button>
                  <span className="text-xs font-mono font-bold text-zinc-550">
                    Progresso: {Math.round((readerCurrentPage / totalPages) * 100)}%
                  </span>
                  <button
                    onClick={() => handleReaderPageChange(1, totalPages)}
                    disabled={readerCurrentPage === totalPages}
                    className="rounded-xl border border-zinc-250 py-2 px-4 text-xs font-bold text-zinc-705 bg-white dark:border-zinc-801 dark:bg-zinc-900 dark:text-zinc-300 hover:bg-zinc-50 disabled:opacity-40"
                  >
                    Próxima Página
                  </button>
                </div>
              </div>
            );
          })()}
        </ModalComponent>
      )}

    </div>
  );
}
