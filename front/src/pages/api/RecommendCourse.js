import './RecommendCourse.css';
import {useState} from "react";
import api from "../../api";

function RecommendCourse() {

    let [condition, setCondition] = useState("");
    let [goal, setGoal] = useState("");
    let [recommendation, setRecommendation] = useState("");

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
                            setRecommendation(result.data);
                        })
                        .catch((error) => {
                            console.error("요가 수업 추천 중 에러 발생:", error.response ? error.response.data : error.message);
                        });
                }}>추천하기</button>
            </div>
            <div className="recommendation-box">
                {recommendation}
            </div>
        </div>
    );
}

export default RecommendCourse;