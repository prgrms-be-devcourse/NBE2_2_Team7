import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import Map from './KakaoMap'; // Map 컴포넌트 임포트
import { FaUserCircle } from 'react-icons/fa'; // 프로필 아이콘 임포트

const BoardListPage = ({ memberName }) => {
    const memberId = 1;
    const [boards, setBoards] = useState([]);
    const [filteredBoards, setFilteredBoards] = useState([]);
    const [page, setPage] = useState(1);
    const [size, setSize] = useState(5);
    const [searchLocation, setSearchLocation] = useState('');
    const [mapCenter, setMapCenter] = useState({ lat: 37.5665, lng: 126.978 }); // 초기 지도 중심 설정
    const [mapLevel, setMapLevel] = useState(9); // 지도 레벨 상태 추가
    const [showMyBoards, setShowMyBoards] = useState(false); // 내 작성글 보기 상태

    useEffect(() => {
        if (showMyBoards) {
            fetchMyBoards(); // 내 게시글 목록 가져오기
        } else {
            fetchBoards(); // 전체 게시글 목록 가져오기
        }
    }, [page, size, showMyBoards]);

    useEffect(() => {
        setFilteredBoards(boards);
    }, [boards]);

    const fetchBoards = async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/board', {
                params: { page, size },
            });
            setBoards(response.data.content);
        } catch (error) {
            console.error('Error fetching boards:', error);
        }
    };

    const fetchMyBoards = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/api/board/member/${memberId}`, {
                params: { page, size },
            });
            setBoards(response.data.content);
        } catch (error) {
            console.error('Error fetching my boards:', error);
        }
    };

    const handleSearch = async () => {
        const kakao = window.kakao;
        const ps = new kakao.maps.services.Places();

        ps.keywordSearch(searchLocation, (data, status) => {
            if (status === kakao.maps.services.Status.OK) {
                const firstPlace = data[0];
                const center = { lat: firstPlace.y, lng: firstPlace.x };

                const nearbyBoards = boards.filter(board => {
                    const distance = getDistance(board.latitude, board.longitude, center.lat, center.lng);
                    return distance <= 5000;
                });

                setFilteredBoards(nearbyBoards);
                setMapCenter(center);
                setMapLevel(5); // 검색 후 지도 확대
            } else {
                setFilteredBoards([]);
                setMapLevel(9); // 초기 레벨로 설정
            }
        });
    };

    const getDistance = (lat1, lon1, lat2, lon2) => {
        const R = 6371e3;
        const φ1 = lat1 * Math.PI / 180;
        const φ2 = lat2 * Math.PI / 180;
        const Δφ = (lat2 - lat1) * Math.PI / 180;
        const Δλ = (lon2 - lon1) * Math.PI / 180;

        const a = Math.sin(Δφ / 2) * Math.sin(Δφ / 2) +
            Math.cos(φ1) * Math.cos(φ2) *
            Math.sin(Δλ / 2) * Math.sin(Δλ / 2);
        const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    };

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
    };

    return (
        <div style={{ display: 'flex' }}>
            {/* 프로필 및 사용자 정보 */}
            <div style={{flex: 1, marginRight: '-900px'}}>
                <div style={{display: 'flex', alignItems: 'center', marginBottom: '20px'}}>
                    <FaUserCircle size={30} style={{marginRight: '10px'}}/>
                    <span>{memberName}</span>
                    <button onClick={() => setShowMyBoards(!showMyBoards)} style={{marginLeft: '20px'}}>
                        {showMyBoards ? '전체 글 보기' : '내 글 보기'}
                    </button>
                </div>

                <h1>{showMyBoards ? '내 글' : '전체 글'}</h1>

                <Link to="/create-board">
                    <button>게시글 작성</button>
                </Link>
                <ul>
                    {filteredBoards.map((board) => (
                        <li key={board.boardId}>
                            <Link to={`/board/${board.boardId}`}>
                            <strong>{board.title}</strong> - {board.nickname} <br/>
                                {board.updatedAt ? (
                                    <span>수정일: {formatDate(board.updatedAt)}</span>
                                ) : (
                                    <span>작성일: {formatDate(board.createdAt)}</span>
                                )}
                                <br/>
                                {board.location && (
                                    <span> 장소: {board.location}</span>
                                )}
                            </Link>
                        </li>
                    ))}
                </ul>
                <div>
                    <button disabled={page === 1} onClick={() => setPage(page - 1)}>
                        이전
                    </button>
                    <button onClick={() => setPage(page + 1)}>다음</button>
                </div>
            </div>
            <div style={{flex: 1}}>
                {/* 지도 영역 */}
                <div>
                    <input
                        type="text"
                        placeholder="위치를 입력하세요"
                        value={searchLocation}
                        onChange={(e) => setSearchLocation(e.target.value)}
                    />
                    <button onClick={handleSearch}>검색</button>
                </div>
                <Map boards={filteredBoards} mapLevel={mapLevel} mapCenter={mapCenter} />
            </div>
        </div>
    );
};

export default BoardListPage;
