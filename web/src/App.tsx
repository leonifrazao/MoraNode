import { BrowserRouter, Routes, Route } from "react-router-dom";
import { MainLayout } from "./components/layout/MainLayout";
import Dashboard from "./pages/Dashboard"; // A tela que já fizemos
import { Contratos } from "./pages/Contratos";
import Imoveis from "./pages/Imoveis";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<MainLayout />}>
          <Route index element={<Dashboard />} />
          <Route path="imoveis" element={<Imoveis />} /> {/* Reaproveitando a lógica de imóveis */}
          <Route path="contratos" element={<Contratos />} />
          <Route path="clientes" element={<div className="text-white">Tela de Clientes (Em breve)</div>} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}