import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import { ProtectedRoute } from "./components/auth/ProtectedRoute";
import { MainLayout } from "./components/layout/MainLayout";
import Dashboard from "./pages/Dashboard"; 
import { Contratos } from "./pages/Contratos";
import Imoveis from "./pages/Imoveis";
import { Login } from "./pages/Login";
import { Register } from "./pages/Register";

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          {/* Rotas Públicas */}
          <Route path="/login" element={<Login />} />
          <Route path="/registro" element={<Register />} />

          {/* Rotas Protegidas e Layout */}
          <Route element={<ProtectedRoute />}>
            <Route path="/" element={<MainLayout />}>
              <Route index element={<Dashboard />} />
              <Route path="imoveis" element={<Imoveis />} />
              <Route path="contratos" element={<Contratos />} />
              <Route path="clientes" element={<div className="text-foreground">Tela de Clientes (Em breve)</div>} />
            </Route>
          </Route>

          {/* Rota Coringa - Se der erro ou não existir, manda pra root (ou login) */}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}