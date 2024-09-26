import React from 'react';
import CommentPage from './comment/CommentPage';

const App = () => {
  const boardId = 2; // Replace with the actual boardId

  return (
      <div>
        <CommentPage boardId={boardId} />
      </div>
  );
};

export default App;
