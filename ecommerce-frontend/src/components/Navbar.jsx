import React from 'react';
import { Link } from 'react-router-dom';
import { useCart } from '../CartContext'; // Import the hook

const Navbar = () => {
    // Pull the total directly from the global state!
    const { totalCartItems } = useCart(); 

    return (
        <header style={{ display: 'flex', justifyContent: 'space-between', padding: '1rem 2rem', backgroundColor: '#2c3e50', color: 'white' }}>
            <Link to="/" style={{ color: 'white', textDecoration: 'none' }}>
                <h2 style={{ margin: 0 }}>OmniShop</h2>
            </Link>
            <div style={{ display: 'flex', gap: '20px', alignItems: 'center' }}>
                <Link to="/cart" style={{ color: 'white', textDecoration: 'none', fontWeight: 'bold' }}>
                    🛒 Cart ({totalCartItems})
                </Link>
                <span style={{ cursor: 'pointer' }}>👤 User</span>
            </div>
        </header>
    );
};

export default Navbar;