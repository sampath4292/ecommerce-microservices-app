import React, { createContext, useState, useContext } from 'react';

// 1. Create the Context
const CartContext = createContext();

// 2. Create a Provider Component
export const CartProvider = ({ children }) => {
    const [cart, setCart] = useState({});

    const handleIncrease = (productId) => {
        setCart(prev => ({ ...prev, [productId]: (prev[productId] || 0) + 1 }));
    };

    const handleDecrease = (productId) => {
        setCart(prev => {
            const newCart = { ...prev };
            if (newCart[productId] > 1) {
                newCart[productId] -= 1;
            } else {
                delete newCart[productId];
            }
            return newCart;
        });
    };

    const emptyCart = () => setCart({});

    const totalCartItems = Object.values(cart).reduce((sum, count) => sum + count, 0);

    // 3. Expose the state and functions to the rest of the app
    return (
        <CartContext.Provider value={{ 
            cart, 
            handleIncrease, 
            handleDecrease, 
            emptyCart, 
            totalCartItems 
        }}>
            {children}
        </CartContext.Provider>
    );
};

// 4. Custom hook for easy importing
export const useCart = () => useContext(CartContext);