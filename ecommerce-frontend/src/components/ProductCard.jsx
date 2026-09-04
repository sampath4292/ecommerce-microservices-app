import React from 'react';

const ProductCard = ({ product, quantity, onIncrease, onDecrease }) => {
    return (
        <div style={{ border: '1px solid #ccc', padding: '1rem', margin: '1rem', width: '200px' }}>
            <h3>{product.name}</h3>
            <p>${product.price}</p>
            
            <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                {quantity > 0 && (
                    <button onClick={() => onDecrease(product.productId)}>-</button>
                )}
                <span>{quantity}</span>
                <button onClick={() => onIncrease(product.productId)}>+</button>
            </div>
        </div>
    );
};

export default ProductCard;