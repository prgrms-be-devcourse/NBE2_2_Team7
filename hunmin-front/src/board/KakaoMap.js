import { Map, MapMarker } from "react-kakao-maps-sdk";
import React from 'react';

const KakaoMap = ({ boards, mapLevel, mapCenter }) => {
    return (
        <div>
            <Map
                center={mapCenter} // props로 전달된 mapCenter 사용
                style={{
                    width: '600px',
                    height: '500px',
                    borderRadius: '20px',
                }}
                level={mapLevel} // props로 전달된 mapLevel 사용
            >
                {boards.map((board) => (
                    board.location && (
                        <MapMarker
                            key={board.boardId}
                            position={{ lat: board.latitude, lng: board.longitude }} // 게시글의 위도와 경도
                            onClick={() => window.location.href = `/board/${board.boardId}`} // 클릭 시 게시글 페이지로 이동
                        >
                            {board.title}
                        </MapMarker>
                    )
                ))}
            </Map>
        </div>
    );
};

export default KakaoMap;
