import React, { Component } from 'react';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';

class BoardWrite extends Component {
    constructor(props) {
        super(props);
        this.quillRef = React.createRef();
    }

    componentDidMount() {
        this.attachImageHandler();
    }

    attachImageHandler = () => {
        const quillEditor = this.quillRef.getEditor();
        const toolbar = quillEditor.getModule('toolbar');
        toolbar.addHandler('image', this.handleImage);
    };

    handleImage = () => {
        const input = document.createElement('input');
        input.setAttribute('type', 'file');
        input.setAttribute('accept', 'image/*');
        input.click();

        input.onchange = async () => {
            const file = input.files[0];
            const imgUrl = await this.props.uploadImage(file); // 이미지 업로드 및 URL 획득

            if (imgUrl) {
                const editor = this.quillRef.getEditor();
                const range = editor.getSelection(true); // Range를 true로 설정하여 포커스 없이 선택

                // imgUrl이 올바른지 확인하기 위해 콘솔에 출력
                console.log('Inserting image with URL:', imgUrl);
                editor.insertEmbed(range.index, 'image', `http://localhost:8080${imgUrl}`); // URL을 사용하여 이미지 추가
                editor.setSelection(range.index + 1); // 이미지 다음 위치로 커서 이동
                this.props.setImageUrls((prev) => [...prev, imgUrl]); // 이미지 URL을 상태에 추가
            }
        };
    };

    modules = {
        toolbar: [
            [{ header: [1, 2, false] }],
            ['bold', 'italic', 'underline', 'strike', 'blockquote'],
            [{ list: 'ordered' }, { list: 'bullet' }, { indent: '-1' }, { indent: '+1' }],
            ['link', 'image'],
            [{ align: [] }, { color: [] }, { background: [] }],
            ['clean'],
        ],
    };

    formats = ['header', 'bold', 'italic', 'underline', 'strike', 'blockquote', 'list', 'bullet', 'indent', 'link', 'image', 'align', 'color', 'background'];

    render() {
        const { value, onChange } = this.props;
        return (
            <div style={{ height: '650px', width: '100%' }}>
                <ReactQuill
                    ref={(el) => {
                        this.quillRef = el;
                    }}
                    style={{ height: '600px' }}
                    theme="snow"
                    modules={this.modules}
                    formats={this.formats}
                    value={value || ''}
                    onChange={(content, delta, source, editor) => onChange(editor.getHTML())}
                />
                <style>
                    {`
                        .quill-image {
                            max-width: 100%; /* 최대 너비를 100%로 설정하여 컨테이너에 맞게 조정 */
                            height: auto; /* 높이는 자동으로 조정 */
                            resize: both; /* 너비와 높이를 모두 조정 가능하게 설정 */
                            overflow: auto; /* 오버플로우를 자동으로 설정하여 크기 조절 시 스크롤 가능하게 */
                            border: 1px solid #ccc; /* 선택된 이미지에 경계선 추가 */
                            display: block; /* 블록 요소로 설정하여 별도의 줄에 표시 */
                        }
                    `}
                </style>
            </div>
        );
    }
}

export default BoardWrite;
