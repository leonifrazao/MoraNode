import { Link, Outlet, useLocation } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { LayoutDashboard, Building2, FileText, Users } from "lucide-react";
import logo from "@/assets/moranodelogo.png";

export function MainLayout() {
    const { pathname } = useLocation();

    const menuItems = [
        { label: "Dashboard", icon: LayoutDashboard, path: "/" },
        { label: "Imóveis", icon: Building2, path: "/imoveis" },
        { label: "Contratos", icon: FileText, path: "/contratos" },
        { label: "Clientes", icon: Users, path: "/clientes" },
    ];

    return (
        <div className="flex min-h-screen bg-background font-sans antialiased text-foreground">
            <aside className="w-64 border-r border-border p-6 flex flex-col gap-8 bg-card/30 sticky top-0 h-screen">
                <div className="flex items-center px-2">
                    <img src={logo} alt="MoraNode" className="h-25 w-auto object-contain" />
                </div>
                <nav className="flex flex-col gap-2">
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
            </aside>
            <main className="flex-1 p-8 overflow-auto">
                <Outlet />
            </main>
        </div>
    );
}