import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuthStore } from '../store/useAuthStore';
import { useNotificationStore } from '../store/useNotificationStore';
import { UserType } from '../types';
import { BookOpen, User, Mail, ShieldCheck, Key, Store, BookHeart, UserCheck, MessageSquare, MapPin } from 'lucide-react';

export default function RegisterPage() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  
  // Custom type states
  const [userType, setUserType] = useState<UserType>('reader');
  const [companyName, setCompanyName] = useState('');
  const [city, setCity] = useState('');
  const [state, setState] = useState('');
  const [whatsapp, setWhatsapp] = useState('');

  const login = useAuthStore((state) => state.login);
  const showToast = useNotificationStore((state) => state.showToast);
  const navigate = useNavigate();

  const handleRegister = (e: React.FormEvent) => {
    e.preventDefault();

    if (!name.trim() || !email.trim() || !password.trim() || !confirmPassword.trim()) {
      showToast('Todos os campos são obrigatórios.', 'warning');
      return;
    }

    if (password !== confirmPassword) {
      showToast('As senhas não coincidem.', 'error');
      return;
    }

    if ((userType === 'bookstore' || userType === 'publisher') && !companyName.trim()) {
      showToast('Por favor, indique a Razão Social ou Nome de sua Empresa.', 'warning');
      return;
    }

    // Register user by triggering auth store login with type settings
    login(email, name, userType, {
      companyName: userType !== 'reader' ? (companyName || name) : undefined,
      city: city || undefined,
      state: state || undefined,
      whatsapp: whatsapp || undefined,
      phone: whatsapp || undefined,
      bio: userType === 'reader' 
        ? 'Leitor voraz atrás de novas descobertas e livros usados especiais.' 
        : userType === 'bookstore' 
        ? `Livraria especializada trazendo títulos selecionados de ${city || 'nossa sede'}.`
        : userType === 'publisher'
        ? 'Editora literária comprometida com a curadoria de títulos extraordinários.'
        : `Autor independente publicando obras originais diretamente aos leitores.`
    });

    const successMessage = userType === 'reader'
      ? `Bem-vindo a bordo, ${name}! Explore novos livros e anuncie seus livros usados.`
      : userType === 'bookstore'
      ? `Livraria "${companyName || name}" cadastrada com sucesso! Comece a anunciar seus títulos de estoque local.`
      : userType === 'publisher'
      ? `Ecossistema de Editora criado com sucesso para "${companyName || name}"!`
      : `Perfil de Autor Independente criado para "${name}"! Publique suas próprias obras diretamente para os leitores.`;

    showToast(successMessage, 'success');
    navigate('/dashboard');
  };

  return (
    <div className="min-h-[85vh] grid grid-cols-1 md:grid-cols-12 rounded-3xl overflow-hidden border border-zinc-150 dark:border-zinc-805 bg-white dark:bg-zinc-950 shadow-xl" id="bookverse-register-form">
      
      {/* Visual background page split */}
      <div className="hidden md:flex md:col-span-5 relative bg-gradient-to-br from-blue-700 via-indigo-900 to-amber-950 p-8 flex-col justify-between text-white text-left select-none">
        <div className="absolute top-0 bottom-0 left-0 w-3.5 bg-black/15" />
        <div className="absolute top-0 bottom-0 left-3.5 w-[0.5px] bg-white/5" />
        
        <div className="flex items-center gap-2">
          <BookOpen className="h-6 w-6 text-amber-405" />
          <span className="font-bold text-sm tracking-widest font-mono uppercase">Livraria Mulemba</span>
        </div>

        <div className="my-auto space-y-4">
          <h2 className="text-3xl font-black font-sans leading-tight">Conectando a Rede Literária</h2>
          <p className="text-xs text-zinc-355 leading-relaxed">
            A plataforma definitiva para conectar leitores, sebos locais, livrarias físicas, autores independentes e editoras parceiras de todo o país. Anuncie livros usados na hora, impulsione suas vendas ou auto-publique no formato digital com royalties diretos.
          </p>
          <div className="grid grid-cols-2 gap-2 text-[10px] font-mono border-t border-white/10 pt-4 mt-2">
            <div>
              <span className="text-amber-400 font-bold block">✓ LIVRARIAS & SEBOS</span>
              <span>Anuncie estoques locais</span>
            </div>
            <div>
              <span className="text-amber-400 font-bold block">✓ GRANDES EDITORAS</span>
              <span>Distribuição direto ao leitor</span>
            </div>
            <div>
              <span className="text-amber-400 font-bold block">✓ LIVROS USADOS</span>
              <span>Intermedeie seu próprio sebo</span>
            </div>
            <div>
              <span className="text-amber-400 font-bold block">✓ ESCRITA AUTÔNOMA</span>
              <span>Receba royalties sem taxas</span>
            </div>
          </div>
        </div>

        <p className="text-[10px] font-mono text-zinc-400">
          CADASTRO SEGURO DE LEITORES E PARCEIROS SHA-255
        </p>
      </div>

      {/* CORE REGISTRATION FORM COLUMN */}
      <div className="md:col-span-7 p-6 sm:p-12 flex flex-col justify-center bg-white dark:bg-zinc-900 scrollbar-thin">
        <div className="max-w-md w-full mx-auto space-y-5">
          <div className="text-left">
            <h1 className="text-3xl font-extrabold text-zinc-901 dark:text-zinc-50 font-sans">Faça Parte da Rede</h1>
            <p className="text-xs text-zinc-401 mt-1">Escolha o tipo de perfil ideal para começar a usar a plataforma.</p>
          </div>

          <form onSubmit={handleRegister} className="space-y-4">
            
            {/* SEGMENTED USER TYPE SELECTOR */}
            <div>
              <label className="text-[10px] text-zinc-400 font-bold font-mono uppercase block mb-2">Quero me cadastrar como: *</label>
              <div className="grid grid-cols-2 gap-2">
                <button
                  type="button"
                  onClick={() => setUserType('reader')}
                  className={`flex flex-col items-start p-3 rounded-xl border text-left cursor-pointer transition-all ${
                    userType === 'reader'
                      ? 'border-blue-600 bg-blue-50/20 text-blue-600 dark:border-blue-500'
                      : 'border-zinc-200 hover:border-zinc-300 text-zinc-650 bg-white dark:border-zinc-800 dark:bg-zinc-950 dark:text-zinc-300'
                  }`}
                >
                  <UserCheck className="h-5 w-5 mb-1.5 text-blue-500" />
                  <span className="text-xs font-bold block">Leitor & Vendedor</span>
                  <span className="text-[9px] text-zinc-400 font-mono mt-0.5 leading-tight font-medium">Compre e venda livros usados</span>
                </button>

                <button
                  type="button"
                  onClick={() => setUserType('bookstore')}
                  className={`flex flex-col items-start p-3 rounded-xl border text-left cursor-pointer transition-all ${
                    userType === 'bookstore'
                      ? 'border-blue-600 bg-blue-50/20 text-blue-600 dark:border-blue-500'
                      : 'border-zinc-200 hover:border-zinc-300 text-zinc-650 bg-white dark:border-zinc-800 dark:bg-zinc-950 dark:text-zinc-300'
                  }`}
                >
                  <Store className="h-5 w-5 mb-1.5 text-blue-500" />
                  <span className="text-xs font-bold block">Livraria de Bairro</span>
                  <span className="text-[9px] text-zinc-400 font-mono mt-0.5 leading-tight font-medium">Sebos e lojas físicas</span>
                </button>

                <button
                  type="button"
                  onClick={() => setUserType('publisher')}
                  className={`flex flex-col items-start p-3 rounded-xl border text-left cursor-pointer transition-all ${
                    userType === 'publisher'
                      ? 'border-blue-600 bg-blue-50/20 text-blue-600 dark:border-blue-500'
                      : 'border-zinc-200 hover:border-zinc-300 text-zinc-650 bg-white dark:border-zinc-800 dark:bg-zinc-950 dark:text-zinc-300'
                  }`}
                >
                  <BookOpen className="h-5 w-5 mb-1.5 text-blue-500" />
                  <span className="text-xs font-bold block">Selo Editorial</span>
                  <span className="text-[9px] text-zinc-400 font-mono mt-0.5 leading-tight font-medium">Editoras e distribuidores</span>
                </button>

                <button
                  type="button"
                  onClick={() => setUserType('author')}
                  className={`flex flex-col items-start p-3 rounded-xl border text-left cursor-pointer transition-all ${
                    userType === 'author'
                      ? 'border-blue-600 bg-blue-50/20 text-blue-600 dark:border-blue-500'
                      : 'border-zinc-200 hover:border-zinc-300 text-zinc-650 bg-white dark:border-zinc-800 dark:bg-zinc-950 dark:text-zinc-300'
                  }`}
                >
                  <BookHeart className="h-5 w-5 mb-1.5 text-blue-500" />
                  <span className="text-xs font-bold block">Autor Independente</span>
                  <span className="text-[9px] text-zinc-400 font-mono mt-0.5 leading-tight font-medium">Publique sem intermediários</span>
                </button>
              </div>
            </div>

            {/* CONDITIONAL EXTRA BUSINESS FIELDS */}
            {(userType === 'bookstore' || userType === 'publisher') && (
              <div className="space-y-3 p-3.5 bg-zinc-50 dark:bg-zinc-950 rounded-2xl border border-zinc-150 dark:border-zinc-805 animate-fade-in">
                <span className="text-[10px] text-zinc-400 font-bold font-mono uppercase block">Dados Empresariais</span>
                <div>
                  <label className="text-[10px] text-zinc-500 font-bold font-mono uppercase block mb-1">Nome da Empresa / Selo Editorial *</label>
                  <input
                    type="text"
                    required
                    value={companyName}
                    onChange={(e) => setCompanyName(e.target.value)}
                    placeholder={userType === 'bookstore' ? 'Livraria da Vila Ltda.' : 'Editora Sextante'}
                    className="w-full rounded-xl border border-zinc-250 bg-white px-3 py-2 text-xs text-zinc-900 outline-none focus:border-blue-600 dark:border-zinc-800 dark:bg-zinc-900 dark:text-zinc-100"
                  />
                </div>
              </div>
            )}

            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="text-[10px] text-zinc-500 font-bold font-mono uppercase block mb-1">Nome Completo *</label>
                <div className="relative flex items-center">
                  <input
                    type="text"
                    required
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    placeholder="Arthur Dent"
                    className="w-full rounded-xl border border-zinc-250 bg-white px-3 py-2 pl-9 text-xs text-zinc-900 outline-none focus:border-blue-600 dark:border-zinc-800 dark:bg-zinc-950 dark:text-zinc-100"
                  />
                  <User className="absolute left-3 h-4 w-4 text-zinc-350" />
                </div>
              </div>

              <div>
                <label className="text-[10px] text-zinc-500 font-bold font-mono uppercase block mb-1">E-mail para Acesso *</label>
                <div className="relative flex items-center">
                  <input
                    type="email"
                    required
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="arthur@galaxy.com"
                    className="w-full rounded-xl border border-zinc-250 bg-white px-3 py-2 pl-9 text-xs text-zinc-900 outline-none focus:border-blue-600 dark:border-zinc-800 dark:bg-zinc-950 dark:text-zinc-100"
                  />
                  <Mail className="absolute left-3 h-4 w-4 text-zinc-350" />
                </div>
              </div>
            </div>

            {/* REGION AND CONTACT (WIDELY USED FOR USED BOOKS / BOOKSTORES) */}
            <div className="grid grid-cols-3 gap-3">
              <div className="col-span-2">
                <label className="text-[10px] text-zinc-500 font-bold font-mono uppercase block mb-1">Cidade</label>
                <div className="relative flex items-center">
                  <input
                    type="text"
                    value={city}
                    onChange={(e) => setCity(e.target.value)}
                    placeholder="São Paulo ou RJ"
                    className="w-full rounded-xl border border-zinc-250 bg-white px-3 py-2 pl-9 text-xs text-zinc-900 outline-none focus:border-blue-600 dark:border-zinc-800 dark:bg-zinc-950 dark:text-zinc-100"
                  />
                  <MapPin className="absolute left-3 h-4 w-4 text-zinc-350" />
                </div>
              </div>

              <div>
                <label className="text-[10px] text-zinc-500 font-bold font-mono uppercase block mb-1">Estado / UF</label>
                <input
                  type="text"
                  maxLength={2}
                  value={state}
                  onChange={(e) => setState(e.target.value.toUpperCase())}
                  placeholder="SP"
                  className="w-full rounded-xl border border-zinc-250 bg-white px-3 py-2 text-center text-xs text-zinc-900 outline-none focus:border-blue-600 dark:border-zinc-800 dark:bg-zinc-950 dark:text-zinc-100"
                />
              </div>
            </div>

            <div>
              <label className="text-[10px] text-zinc-500 font-bold font-mono uppercase block mb-1">WhatsApp / Telefone para Contato</label>
              <div className="relative flex items-center">
                <input
                  type="text"
                  value={whatsapp}
                  onChange={(e) => setWhatsapp(e.target.value)}
                  placeholder="+55 (11) 99999-9999"
                  className="w-full rounded-xl border border-zinc-250 bg-white px-3 py-2 pl-9 text-xs text-zinc-900 outline-none focus:border-blue-600 dark:border-zinc-800 dark:bg-zinc-950 dark:text-zinc-100"
                />
                <MessageSquare className="absolute left-3 h-4 w-4 text-zinc-350" />
              </div>
            </div>

            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="text-[10px] text-zinc-500 font-bold font-mono uppercase block mb-1">Senha de Entrada *</label>
                <div className="relative flex items-center">
                  <input
                    type="password"
                    required
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="Mínimo de 8 caracteres..."
                    className="w-full rounded-xl border border-zinc-250 bg-white px-3 py-2 pl-9 text-xs text-zinc-900 outline-none focus:border-blue-600 dark:border-zinc-800 dark:bg-zinc-950 dark:text-zinc-100"
                  />
                  <Key className="absolute left-3 h-4 w-4 text-zinc-350" />
                </div>
              </div>

              <div>
                <label className="text-[10px] text-zinc-500 font-bold font-mono uppercase block mb-1">Confirme sua Senha *</label>
                <div className="relative flex items-center">
                  <input
                    type="password"
                    required
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    placeholder="Repita a senha..."
                    className="w-full rounded-xl border border-zinc-250 bg-white px-3 py-2 pl-9 text-xs text-zinc-900 outline-none focus:border-blue-605 dark:border-zinc-800 dark:bg-zinc-950 dark:text-zinc-100"
                  />
                  <Key className="absolute left-3 h-4 w-4 text-zinc-350" />
                </div>
              </div>
            </div>

            <button
              type="submit"
              className="w-full rounded-xl bg-blue-600 hover:bg-blue-700 py-3 text-xs font-bold text-white shadow-md cursor-pointer transition"
            >
              Criar Minha Conta na Livraria Mulemba
            </button>
          </form>

          <p className="text-center text-xs text-zinc-500">
            Já possui cadastro na Livraria Mulemba?{' '}
            <Link to="/login" className="text-blue-605 font-bold hover:underline">
              Ir para o Login
            </Link>
          </p>

        </div>
      </div>

    </div>
  );
}
