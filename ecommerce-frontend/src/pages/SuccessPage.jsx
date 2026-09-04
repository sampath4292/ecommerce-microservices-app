import React from 'react';
import { useLocation, Link } from 'react-router-dom';

const SuccessPage = () => {
    // Extract the success message passed from the CartPage navigation
    const location = useLocation();
    const message = location.state?.message || "Your order has been placed!";

    return (
        <div style={{ textAlign: 'center', marginTop: '4rem' }}>
            <h1 style={{ color: '#28a745' }}>🎉 Success!</h1>
            <p style={{ fontSize: '1.2rem' }}>{message}</p>
            
            <Link to="/" style={{ 
                display: 'inline-block', 
                marginTop: '2rem', 
                padding: '10px 20px', 
                backgroundColor: '#007bff', 
                color: 'white', 
                textDecoration: 'none', 
                borderRadius: '5px' 
            }}>
                Continue Shopping
            </Link>
        </div>
    );
};

export default SuccessPage;