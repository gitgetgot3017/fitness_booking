import './RecommendCourse.css';
import {useState} from "react";
import api from "../../api";
import {useNavigate} from "react-router-dom";

function RecommendCourse() {

    let [courseId, setCourseId] = useState(0);
    let [condition, setCondition] = useState("");
    let [goal, setGoal] = useState("");
    let [recommendDto, setRecommendDto] = useState({"geminiSaid": null, "courseName": null});

    let navigate = useNavigate();

    function getFormattedTodayDate() {
        return new Intl.DateTimeFormat("ko-KR", { year: "numeric", month: "2-digit", day: "2-digit" })
            .format(new Date())
            .trim()
            .replace(/\.\s*/g, "-")
            .replace(/-$/, "");
    }

    return (
        <div className="container" style={{textAlign: "center"}}>
            <h1>요가 수업 추천</h1>
            <div className="form-container">
                <h2>컨디션과 목표를 선택하세요</h2>
                <select id="condition" onChange={(e) => {
                    setCondition(e.target.value);
                }}>
                    <option value="">컨디션 선택</option>
                    <option value="GOOD">좋아요</option>
                    <option value="SOSO">보통이에요</option>
                    <option value="BAD">나빠요</option>
                </select>
                <select id="goal" onChange={(e) => {
                    setGoal(e.target.value);
                }}>
                    <option value="">목표 선택</option>
                    <option value="FLEXIBILITY">유연성 증가</option>
                    <option value="WEIGHT">체중 감량</option>
                    <option value="STRENGTH">체력 향상</option>
                    <option value="MIND">정신 수련</option>
                </select>
                <button onClick={() => {
                    api.get("/api/ai", {
                            params: {
                                condition: condition,
                                goal: goal
                            }
                        })
                        .then((result) => {
                            setRecommendDto(result.data);
                        })
                        .catch((error) => {
                            console.error("요가 수업 추천 중 에러 발생:", error.response ? error.response.data : error.message);
                        });
                }}>추천하기</button>
            </div>
            <div className="recommendation-box">
                {recommendDto.geminiSaid}
                <br />
                {
                    recommendDto.courseName === null ?
                        null :
                        <button style={{marginTop: "20px"}} onClick={() => {
                            api.get("/courses/id?name=" + recommendDto.courseName)
                                .then((result) => {
                                    setCourseId(result.data);

                                    api.post("/courses/reservations", {
                                            date: getFormattedTodayDate(),
                                            courseId: courseId
                                        }, {
                                            headers: { "Content-Type": "application/json" }
                                        })
                                        .then(() => {
                                            alert("수업을 예약하였습니다!");
                                            navigate("/");
                                        })
                                        .catch((error) => {
                                            console.error("추천 수업 예약 중 에러 발생:", error.response ? error.response.data : error.message);
                                            alert(error.response.data.message);
                                        })
                                })
                                .catch((error) => {
                                    console.error("요가 수업 추천 중 에러 발생:", error.response ? error.response.data : error.message);
                                });
                        }}>{recommendDto.courseName} 예약하기</button>
                }
            </div>
        </div>
    );
}

export default RecommendCourse;