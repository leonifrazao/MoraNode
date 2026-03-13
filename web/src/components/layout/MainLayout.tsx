import { Link, Outlet, useLocation } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { LayoutDashboard, Building2, FileText, Users, LogOut, UserCircle } from "lucide-react";
import logo from "@/assets/moranodelogo.png";
import { useAuth } from "../../context/AuthContext";

export function MainLayout() {
    const { pathname } = useLocation();
    const { user, logout } = useAuth(); // <--- pegando estado do authContext

    const menuItems = [
        { label: "Dashboard", icon: LayoutDashboard, path: "/" },
        { label: "Imóveis", icon: Building2, path: "/imoveis" },
        { label: "Contratos", icon: FileText, path: "/contratos" },
        { label: "Clientes", icon: Users, path: "/clientes" },
    ];

    return (
        <div className="flex min-h-screen bg-background font-sans antialiased text-foreground">
            <aside className="w-64 border-r border-border p-6 flex flex-col bg-card/30 sticky top-0 h-screen">
                <div className="flex items-center px-2 mb-8">
                    <img src={logo} alt="MoraNode" className="h-25 w-auto object-contain" />
                </div>
                
                <nav className="flex flex-col gap-2 flex-1">
                    {menuItems.map((item) => (
                        <Button
                            key={item.path}
                            variant={pathname === item.path ? "secondary" : "ghost"}
                            className={`justify-start gap-3 ${pathname !== item.path && "text-muted-foreground"}`}
                            asChild
                        >
                            <Link to={item.path}>
                                <item.icon size={20} />
                                <span className="font-medium">{item.label}</span>
                            </Link>
                        </Button>
                    ))}
                </nav>

                {/* Perfil e Botão de Sair fixado no rodapé da Sidebar */}
                <div className="mt-auto border-t border-border pt-4">
                    {user && (
                        <div className="px-2 py-3 mb-2 flex items-center gap-3">
                            <UserCircle size={36} className="text-muted-foreground" />
                            <div className="flex flex-col overflow-hidden">
                                <span className="text-sm font-semibold truncate leading-none mb-1 text-foreground" title={user.sub}>
                                    {user.sub.split('@')[0]}
                                </span>
                                <span className="text-xs text-muted-foreground leading-none">
                                    {user.papel}
                                </span>
                            </div>
                        </div>
                    )}
                    <Button 
                        variant="ghost" 
                        className="w-full justify-start gap-3 text-destructive hover:text-destructive hover:bg-destructive/10"
                        onClick={logout}
                    >
                        <LogOut size={20} />
                        <span className="font-medium">Sair da Conta</span>
                    </Button>
                </div>
            </aside>
            <main className="flex-1 p-8 overflow-auto">
                <Outlet />
            </main>
        </div>
    );
}