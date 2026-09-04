import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { CartProvider } from './CartContext';
import Navbar from './components/Navbar';
import ProductsPage from './pages/ProductsPage';
import CartPage from './pages/CartPage';
import SuccessPage from './pages/SuccessPage';

const App = () => {
    return (
        <CartProvider>
            <Router>
                <Navbar />
                <Routes>
                    <Route path="/" element={<ProductsPage />} />
                    <Route path="/cart" element={<CartPage />} />
                    <Route path="/success" element={<SuccessPage />} />
                </Routes>
            </Router>
        </CartProvider>
    );
};

export default App;