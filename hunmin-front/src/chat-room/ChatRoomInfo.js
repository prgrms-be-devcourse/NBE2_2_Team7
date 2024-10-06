import React, { useEffect, useState } from 'react';
import api from '../axios'; // axios 인스턴스 import

const ChatRoomInfo = ({ chatRoomId, setNickname }) => {
    const [loading, setLoading] = useState(true); // 로딩 상태 관리

    // API를 통해 채팅방 정보 가져오기
    useEffect(() => {
        const fetchRoomDetails = async () => {
            try {
                const response = await api.get(`/chat-room/${chatRoomId}`); // API로 채팅방 정보 가져오기
                setNickname(response.data.nickname); // 부모 컴포넌트의 setNickname 호출
                console.info('response by one chatRoom info={}', response.data.nickname);
            } catch (error) {
                console.error('채팅방 정보를 불러오는 데 실패했습니다.', error);
            } finally {
                setLoading(false); // 로딩 완료
            }
        };

        fetchRoomDetails(); // 컴포넌트 마운트 시 API 호출
    }, [chatRoomId, setNickname]);

    if (loading) return <div>로딩 중...</div>; // 로딩 중일 때 로딩 메시지 반환

    return null; // 로딩이 끝나면 아무것도 렌더링하지 않음 (닉네임은 부모 컴포넌트에서 처리)
};

export default ChatRoomInfo;
