import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { authService } from '../services/api';
import { Button } from '@/components/ui/button';
import logo from '@/assets/moranodelogo.png';

export function Register() {
    const [nome, setNome] = useState('');
    const [email, setEmail] = useState('');
    const [senha, setSenha] = useState('');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState(false);
    const [loading, setLoading] = useState(false);
    
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {
            await authService.registro({ nome, email, senha });
            setSuccess(true);
            setTimeout(() => {
                navigate('/login');
            }, 3000);
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        } catch (err: any) {
            setError(err.response?.data?.message || err.response?.data?.error || 'Erro ao realizar cadastro. Tente outro email.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-background text-foreground relative overflow-hidden">
            <div className="w-full max-w-md p-6 sm:p-8 bg-card border border-border shadow-2xl rounded-2xl relative z-10 animate-fade-in-up">
                <div className="text-center mb-8">
                    <img src={logo} alt="MoraNode" className="h-20 mx-auto object-contain mb-4" />
                    <h2 className="text-3xl font-bold tracking-tight">Criar Conta</h2>
                    <p className="text-muted-foreground mt-2">Junte-se à nova era da gestão imobiliária.</p>
                </div>

                {success ? (
                    <div className="text-center p-6 bg-green-500/10 border border-green-500/20 rounded-xl space-y-4">
                        <div className="text-green-500 text-5xl">✓</div>
                        <h3 className="text-xl font-semibold text-green-500">Conta criada com sucesso!</h3>
                        <p className="text-sm text-muted-foreground">Você será redirecionado para o login em instantes...</p>
                    </div>
                ) : (
                    <form onSubmit={handleSubmit} className="space-y-5">
                        {error && (
                            <div className="p-3 rounded-md bg-destructive/15 text-destructive border border-destructive/20 text-sm">
                                {error}
                            </div>
                        )}
                        
                        <div className="space-y-2">
                            <label className="text-sm font-medium leading-none">Nome Completo</label>
                            <input
                                type="text"
                                value={nome}
                                onChange={(e) => setNome(e.target.value)}
                                placeholder="João da Silva"
                                className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
                                required
                            />
                        </div>

                        <div className="space-y-2">
                            <label className="text-sm font-medium leading-none">Email</label>
                            <input
                                type="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                placeholder="seu@email.com"
                                className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
                                required
                            />
                        </div>

                        <div className="space-y-2">
                            <label className="text-sm font-medium leading-none">Senha</label>
                            <input
                                type="password"
                                value={senha}
                                onChange={(e) => setSenha(e.target.value)}
                                placeholder="Crie uma senha forte"
                                className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
                                required
                            />
                        </div>

                        <Button type="submit" className="w-full h-11 text-base font-semibold" disabled={loading}>
                            {loading ? 'Criando conta...' : 'Cadastrar'}
                        </Button>
                    </form>
                )}

                {!success && (
                    <div className="mt-6 text-center text-sm text-muted-foreground">
                        Já tem uma conta?{' '}
                        <Link to="/login" className="text-primary font-medium hover:underline hover:text-primary/90 transition-colors">
                            Faça login
                        </Link>
                    </div>
                )}
            </div>
        </div>
    );
}
