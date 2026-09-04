import React, { useState, useEffect } from 'react';
import api from '../api/axiosConfig';
import ProductCard from '../components/ProductCard';
import { useCart } from '../CartContext';

const ProductsPage = () => {
    const [products, setProducts] = useState([]);
    
    // Pull exactly what we need from the global context
    const { cart, handleIncrease, handleDecrease } = useCart();

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                // Fetching via the API Gateway on port 8080
                const response = await api.get('/inventory');
                setProducts(response.data);
            } catch (error) {
                console.error("Failed to fetch inventory:", error);
            }
        };
        fetchProducts();
    }, []);

    return (
        <div style={{ display: 'flex', flexWrap: 'wrap', padding: '2rem' }}>
            {products.map(product => (
                <ProductCard 
                    key={product.productId} 
                    product={product} 
                    quantity={cart[product.productId] || 0}
                    onIncrease={handleIncrease}
                    onDecrease={handleDecrease}
                />
            ))}
        </div>
    );
};

export default ProductsPage;