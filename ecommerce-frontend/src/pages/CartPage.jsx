import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axiosConfig';
import { useCart } from '../CartContext';

const CartPage = () => {
    const { cart, emptyCart } = useCart();
    const [products, setProducts] = useState([]);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const response = await api.get('/inventory');
                setProducts(response.data);
            } catch (error) {
                console.error("Failed to fetch inventory:", error);
            }
        };
        fetchProducts();
    }, []);

    const handleCheckout = async () => {
        setIsSubmitting(true);

        const orderItems = Object.keys(cart).map(productId => {
            const product = products.find(p => p.productId === productId);
            return {
                productId: productId,
                quantity: cart[productId],
                price: product.price
            };
        });

        const payload = {
            userId: "USER-REACT-999",
            items: orderItems
        };

        try {
            const response = await api.post('/orders', payload);
            emptyCart(); // Clear the global cart state
            navigate('/success', { state: { message: response.data } });
        } catch (error) {
            console.error("Order failed:", error);
            alert("Checkout failed. Check the console for errors.");
        } finally {
            setIsSubmitting(false);
        }
    };

    if (Object.keys(cart).length === 0) {
        return <h2 style={{ textAlign: 'center', marginTop: '3rem' }}>Your cart is empty.</h2>;
    }

    return (
        <div style={{ padding: '2rem', maxWidth: '600px', margin: '0 auto' }}>
            <h2>Review Your Cart</h2>
            
            {Object.keys(cart).map(productId => {
                const product = products.find(p => p.productId === productId);
                if (!product) return null;
                return (
                    <div key={productId} style={{ display: 'flex', justifyContent: 'space-between', padding: '10px 0', borderBottom: '1px solid #eee' }}>
                        <span>{product.name} (x{cart[productId]})</span>
                        <span>${(product.price * cart[productId]).toFixed(2)}</span>
                    </div>
                );
            })}

            <button 
                onClick={handleCheckout} 
                disabled={isSubmitting}
                style={{
                    width: '100%', padding: '15px', marginTop: '2rem',
                    backgroundColor: '#28a745', color: 'white', border: 'none',
                    fontSize: '18px', cursor: isSubmitting ? 'wait' : 'pointer'
                }}
            >
                {isSubmitting ? "Processing Payment..." : "Confirm & Pay"}
            </button>
        </div>
    );
};

export default CartPage;