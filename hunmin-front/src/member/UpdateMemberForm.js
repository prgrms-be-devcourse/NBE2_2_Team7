import React, { useState } from 'react';
import axios from 'axios';

function UpdateMemberForm() {
    const [memberId, setMemberId] = useState('');
    const [member, setMember] = useState({
        password: '',
        nickname: '',
        country: '',
        level: '',
        image: ''
    });

    const handleIdChange = (e) => {
        setMemberId(e.target.value);
    };

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
        if (member.password) formData.append('password', member.password);
        if (member.nickname) formData.append('nickname', member.nickname);
        if (member.country) formData.append('country', member.country);
        if (member.level) formData.append('level', member.level);
        if (member.image) {
            formData.append('image', member.image);
        }

        axios.put(`/api/members/${memberId}`, formData)
            .then(response => {
                alert('회원정보가 수정되었습니다.');
            })
            .catch(error => {
                console.error(error);
                alert('회원정보 수정에 실패했습니다.');
            });

    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>회원정보 수정</h2>
            <div>
                <label>회원 ID:</label>
                <input type="text" value={memberId} onChange={handleIdChange} required />
            </div>
            <div>
                <label>비밀번호:</label>
                <input type="password" name="password" value={member.password} onChange={handleChange} />
            </div>
            <div>
                <label>닉네임:</label>
                <input type="text" name="nickname" value={member.nickname} onChange={handleChange} />
            </div>
            <div>
                <label>국가:</label>
                <input type="text" name="country" value={member.country} onChange={handleChange} />
            </div>
            <div>
                <label>레벨:</label>
                <input type="text" name="level" value={member.level} onChange={handleChange} />
            </div>
            <div>
                <label>프로필 이미지:</label>
                <input type="file" name="image" onChange={handleFileChange} accept="image/*" />
            </div>
            <button type="submit">수정하기</button>
        </form>
    );
}

export default UpdateMemberForm;
