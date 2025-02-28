import {useState} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";

function Login() {

    let [memberNum, setMemberNum] = useState("");
    let [password, setPassword] = useState("");

    let navigate = useNavigate();

    return (
        <>
            <input type="text" onChange={(e) => setMemberNum(e.target.value)} />
            <input type="password" onChange={(e) => setPassword(e.target.value)} />
            <button onClick={() => {
                axios.post("/api/members/login", {
                        memberNum: memberNum,
                        password: password
                    }, {
                        headers: { "Content-Type": "application/json" }
                    })
                    .then((result) => {
                        window.localStorage.setItem("accessToken", result.data.accessToken);
                        window.localStorage.setItem("refreshToken", result.data.refreshToken);

                        if (result.data.grade === "ADMIN") {
                            let eventSource = new EventSource('http://localhost:8080/api/notifications?accessToken=' + localStorage.getItem("accessToken"));

                            eventSource.onopen = function () {
                                console.log("SSE 연결 성공!");
                            }

                            eventSource.onmessage = function(event) {
                                console.log("서버로부터 데이터를 수신하였음", event.data);
                            }

                            eventSource.onerror = function(error) {
                                console.log("SSE 연결 오류 발생!", error);
                            }
                        }

                        navigate("/");
                    })
                    .catch((error) => {
                        console.error("로그인 중 에러 발생:", error.response ? error.response.data : error.message);
                        if (error.response) {
                            console.error("에러 상태 코드:", error.response.status);
                        }
                    })
            }}>로그인</button>
        </>
    );
}

export default Login;