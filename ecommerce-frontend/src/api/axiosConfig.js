import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api', // Pointing to the API Gateway now!
    headers: {
        'Content-Type': 'application/json',
    },
});

export default api;