import React, { useState } from 'react';
import axios from 'axios';

function RegistrationForm() {
    const [member, setMember] = useState({
        email: '',
        password: '',
        nickname: '',
        country: '',
        level: '',
        image: ''
    });

    const handleChange = (e) => {
        setMember({
            ...member,
            [e.target.name]: e.target.value,
        });
    };

    const handleFileChange = (e) => {
        setMember({
            ...member,
            image: e.target.files[0],
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        // 이미지 파일을 포함하여 폼 데이터를 생성합니다.
        const formData = new FormData();
        formData.append('email', member.email);
        formData.append('password', member.password);
        formData.append('nickname', member.nickname);
        formData.append('country', member.country);
        formData.append('level', member.level);

        axios.post('/api/members/register', formData)
            .then(response => {
                alert(response.data); // "회원 가입이 완료되었습니다."
            })
            .catch(error => {
                console.error(error);
                alert('회원가입에 실패했습니다.');
            });
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>회원가입</h2>
            <div>
                <label>이메일 주소:</label>
                <input type="email" name="email" value={member.email} onChange={handleChange} required />
            </div>
            <div>
                <label>비밀번호:</label>
                <input type="password" name="password" value={member.password} onChange={handleChange} required />
            </div>
            <div>
                <label>닉네임:</label>
                <input type="text" name="nickname" value={member.nickname} onChange={handleChange} required />
            </div>
            <div>
                <label>국가:</label>
                <input type="text" name="country" value={member.country} onChange={handleChange} required />
            </div>
            <div>
                <label>레벨:</label>
                <input type="text" name="level" value={member.level} onChange={handleChange} required />
            </div>
            <button type="submit">회원가입</button>
        </form>
    );
}

export default RegistrationForm;
