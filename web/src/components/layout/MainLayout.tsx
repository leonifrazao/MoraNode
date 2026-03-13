import { useState } from "react";
import { Link, Outlet, useLocation } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { LayoutDashboard, Building2, FileText, Users, LogOut, UserCircle, Menu, X } from "lucide-react";
import logo from "@/assets/moranodelogo.png";
import { useAuth } from "../../context/AuthContext";

export function MainLayout() {
    const { pathname } = useLocation();
    const { user, logout } = useAuth();
    const [sidebarOpen, setSidebarOpen] = useState(false);

    const menuItems = [
        { label: "Dashboard", icon: LayoutDashboard, path: "/" },
        { label: "Imóveis", icon: Building2, path: "/imoveis" },
        { label: "Contratos", icon: FileText, path: "/contratos" },
        { label: "Clientes", icon: Users, path: "/clientes" },
    ];

    const handleNavClick = () => {
        setSidebarOpen(false);
    };

    const sidebarContent = (
        <>
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
                        <Link to={item.path} onClick={handleNavClick}>
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
        </>
    );

    return (
        <div className="flex min-h-screen bg-background font-sans antialiased text-foreground">
            {/* Mobile Header Bar */}
            <div className="fixed top-0 left-0 right-0 z-40 flex items-center justify-between px-4 py-3 bg-card/80 backdrop-blur-md border-b border-border md:hidden">
                <img src={logo} alt="MoraNode" className="h-10 w-auto object-contain" />
                <Button
                    variant="ghost"
                    size="icon"
                    onClick={() => setSidebarOpen(!sidebarOpen)}
                    className="text-foreground"
                >
                    {sidebarOpen ? <X size={24} /> : <Menu size={24} />}
                </Button>
            </div>

            {/* Mobile Backdrop */}
            {sidebarOpen && (
                <div
                    className="fixed inset-0 z-40 bg-black/60 backdrop-blur-sm md:hidden"
                    onClick={() => setSidebarOpen(false)}
                />
            )}

            {/* Sidebar - Desktop: always visible, Mobile: overlay */}
            <aside
                className={`
                    fixed top-0 left-0 z-50 h-screen w-64 border-r border-border p-6 flex flex-col bg-card/95 backdrop-blur-md
                    transition-transform duration-300 ease-in-out
                    md:sticky md:translate-x-0 md:bg-card/30 md:backdrop-blur-none
                    ${sidebarOpen ? "translate-x-0" : "-translate-x-full"}
                `}
            >
                {sidebarContent}
            </aside>

            <main className="flex-1 p-4 pt-20 md:p-8 md:pt-8 overflow-auto">
                <Outlet />
            </main>
        </div>
    );
}