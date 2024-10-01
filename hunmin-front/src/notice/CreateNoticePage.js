import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';
import './CreateNoticePage.css';
import Markdown from 'markdown-to-jsx';
import BoardWrite from '../board/write/BoardWrite'; // BoardWrite 컴포넌트를 임포트합니다.
const CreateNoticePage = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [notice, setNotice] = useState({ memberId: 1, title: '', content: '' });

    useEffect(() => {
        if (id) {
            axios.get(`/api/notices/${id}`)
                .then(response => {
                    setNotice(response.data);
                })
                .catch(error => {
                    console.error("There was an error fetching the notice!", error);
                });
        }
    }, [id]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setNotice(prevNotice => ({
            ...prevNotice,
            [name]: value
        }));
    };

    const handleContentChange = (content) => {
        setNotice(prevNotice => ({
            ...prevNotice,
            content: content
        }));
    };


    const handleSubmit = (e) => {
        e.preventDefault();

        if (id) {
            axios.put(`/api/notices/${id}`, notice)
                .then(response => {
                    navigate(`/notices/${id}`);
                })
                .catch(error => {
                    console.error("There was an error updating the notice!", error);
                });
        } else {
            axios.post('/api/notices', notice)
                .then(response => {
                    navigate(`/notices/${response.data.noticeId}`);
                })
                .catch(error => {
                    console.error("There was an error creating the notice!", error);
                });
        }
    };
    const handleGoHome = () => {
        navigate('/notices');
    };

    const uploadImage = async (file) => {
        const formData = new FormData();
        formData.append('file', file);

        try {
            const response = await axios.post('/api/upload', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
            return response.data.url; // 서버에서 반환된 이미지 URL
        } catch (error) {
            console.error('Image upload failed:', error);
            return null;
        }
    };

    return (
        <div className="form-container">
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label className="label">제목:</label>
                    <input type="text" name="title" value={notice.title} onChange={handleChange} required
                           className="input"/>
                </div>
                <div className="form-group">
                    <label className="label">내용:</label>
                    <BoardWrite
                        value={notice.content}
                        onChange={handleContentChange}
                        uploadImage={uploadImage}
                        setImageUrls={(urls) => setNotice(prevNotice => ({
                            ...prevNotice,
                            imageUrls: urls
                        }))}
                    />
                </div>
                <div className="preview">
                    <h3>미리보기:</h3>
                    <Markdown>{notice.content}</Markdown>
                </div>
                <button type="submit" className="submit-button">{id ? '수정' : '생성'}</button>
            </form>
            <button onClick={handleGoHome} className="home-button">공지사항 목록으로 돌아가기</button>
        </div>
    );

// return (
//     <div className="form-container">
//         <form onSubmit={handleSubmit}>
//             <div className="form-group">
//                 <label className="label">제목:</label>
//                 <input type="text" name="title" value={notice.title} onChange={handleChange} required
//                        className="input"/>
//             </div>
//             <div className="form-group">
//                 <label className="label">내용:</label>
//                 <textarea name="content" value={notice.content} onChange={handleChange} required
//                           className="textarea"/>
//             </div>
//             <button type="submit" className="submit-button">{id ? '수정' : '생성'}</button>
//         </form>
//         <button onClick={handleGoHome} className="home-button">공지사항 목록으로 돌아가기</button>
//     </div>
// );
};

export default CreateNoticePage;