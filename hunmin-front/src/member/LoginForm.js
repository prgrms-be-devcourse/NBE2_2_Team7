import React, { useState } from 'react';
import { TextField, Button, Container, Typography, Box } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const LoginForm = ({ setToken }) => { // setToken을 props로 받기
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/api/members/login', { email, password }, {
                headers: {
                    'Content-Type': 'application/json',
                }
            });
            console.log(response.data);
            const token = response.data.token;
            const memberId = response.data.memberId;
            const memberEmail = response.data.email;
            const role = response.data.role;
            const nickname = response.data.nickname;
            const image = response.data.image;
            const level = response.data.level;
            const country = response.data.country;

            localStorage.setItem('token', token);
            localStorage.setItem('memberId', memberId);
            localStorage.setItem('email', memberEmail);
            localStorage.setItem('role', role);
            localStorage.setItem('nickname', nickname);
            localStorage.setItem('image', image);
            localStorage.setItem('level', level);
            localStorage.setItem('country', country);

            setToken(token); // 상태 업데이트
            navigate('/');
        } catch (error) {
            console.log(error);
            setError('로그인 실패. 다시 시도해주세요.');
        }
    };

    return (
        <Container maxWidth="xs">
            <Box sx={{ mt: 8 }}>
                <Typography variant="h4" component="h1" gutterBottom>
                    로그인
                </Typography>
                <form onSubmit={handleSubmit}>
                    <TextField
                        label="이메일"
                        fullWidth
                        margin="normal"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                    <TextField
                        label="비밀번호"
                        type="password"
                        fullWidth
                        margin="normal"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                    {error && <Typography color="error">{error}</Typography>}
                    <Button type="submit" variant="contained" color="primary" fullWidth sx={{ mt: 2 }}>
                        로그인
                    </Button>
                </form>
                <Button
                    variant="text"
                    color="primary"
                    onClick={() => navigate('/register')}
                    sx={{ mt: 2 }}
                >
                    회원가입
                </Button>
            </Box>
        </Container>
    );
};

export default LoginForm;
