import './RegistierInstructor.css';
import {useState} from "react";
import axios from "axios";

function RegisterInstructor() {

    let [instructorName, setInstructorName] = useState("");
    let [image, setImage] = useState();

    function showPreview(e) {

        const preview = document.getElementById('image-preview');
        preview.innerHTML = '';
        const file = e.target.files[0];
        if (file) {
            setImage(file);

            const reader = new FileReader();
            reader.onload = function(e) {
                const img = document.createElement('img');
                img.src = e.target.result;
                preview.appendChild(img);
            }
            reader.readAsDataURL(file);
        }
    }

    return (
        <div className="container">
            <div className="header">
                <div className="menu">
                    <h1>강사 등록</h1>
                    <span className="hamburger">☰</span>
                </div>
            </div>
            <hr />
            <div className="form-container">
                <h2>강사 등록</h2>
                <form>
                    <input type="text" placeholder="강사 이름" onChange={(e) => setInstructorName(e.target.value)} required />
                    <label htmlFor="instructor-image">강사 이미지 첨부:</label>
                    <label className="file-upload" htmlFor="instructor-image">
                        <svg viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M32 12v28M20 32l12-12 12 12" stroke="#aaa" stroke-width="4" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                        <span>이미지 업로드 (PNG, JPG)</span>
                    </label>
                    <input type="file" id="instructor-image" accept="image/*" hidden required onChange={showPreview} />
                    <div className="preview" id="image-preview"></div>
                    <button onClick={(e) => {
                        e.preventDefault();

                        let formData = new FormData();
                        formData.append("instructorName", instructorName);
                        formData.append("image", image);

                        axios.post("/admin/register/instructors", formData)
                            .then(() => {
                                alert("강사를 등록하였습니다.");
                            })
                            .catch((error) => {
                                console.error("강사 등록 중 에러 발생:", error.response ? error.response.data : error.message);
                                if (error.response) {
                                    console.error("에러 상태 코드:", error.response.status);
                                }
                            });
                    }}>등록</button>
                </form>
            </div>
        </div>
    );
}

export default RegisterInstructor;