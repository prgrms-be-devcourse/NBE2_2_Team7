import React, { useState } from 'react';
import axios from 'axios';

function LoginForm({ onLogin }) {
    const [credentials, setCredentials] = useState({
        email: '',
        password: ''
    });

    const handleChange = (e) => {
        setCredentials({
            ...credentials,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        axios.post('/api/members/login', credentials, {
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                // 서버로부터 받은 토큰을 추출합니다.
                const authHeader = response.headers['authorization'];
                if (authHeader && authHeader.startsWith('Bearer ')) {
                    const token = authHeader.substring(7, authHeader.length); // 'Bearer ' 제거
                    // Axios의 기본 헤더에 Authorization 설정
                    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

                    // 상위 컴포넌트로 토큰 전달 (필요한 경우)
                    onLogin(token);
                } else {
                    alert('토큰을 받지 못했습니다.');
                }
            })
            .catch(error => {
                console.error(error);
                alert('로그인에 실패했습니다.');
            });
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>로그인</h2>
            <div>
                <label>이메일 주소:</label>
                <input type="email" name="email" value={credentials.email} onChange={handleChange} required />
            </div>
            <div>
                <label>비밀번호:</label>
                <input type="password" name="password" value={credentials.password} onChange={handleChange} required />
            </div>
            <button type="submit">로그인</button>
        </form>
    );
}

export default LoginForm;
