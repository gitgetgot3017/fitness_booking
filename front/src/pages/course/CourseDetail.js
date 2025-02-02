import './CourseDetail.css';
import {useEffect, useState} from "react";
import axios from "axios";

function CourseDetail() {

    let [memberName, setMemberName] = useState('');
    let [memberNum, setMemberNum] = useState('');
    let [subscriptionName, setSubscriptionName] = useState('');
    let [startDate, setStartDate] = useState('');
    let [endDate, setEndDate] = useState('');
    let [completedCount, setCompletedCount] = useState(0);
    let [availableCount, setAvailableCount] = useState(0);
    let [reservedCount, setReservedCount] = useState(0);
    let [courseDate, setCourseDate] = useState('');
    let [course, setCourse] = useState(null);
    let [cancelableCount, setCancelableCount] = useState(0);

    let params = new URLSearchParams();
    params.append("date", window.localStorage.getItem("courseDate"));
    params.append("courseId", window.localStorage.getItem("courseId"));

    useEffect(() => {
        axios.get("/courses/detail", { params })
            .then((result) => {
                setMemberName(result.data.memberName);
                setMemberNum(result.data.memberNum);
                setSubscriptionName(result.data.subscriptionName);
                setStartDate(result.data.startDate);
                setEndDate(result.data.endDate);
                setCompletedCount(result.data.completedCount);
                setAvailableCount(result.data.availableCount);
                setReservedCount(result.data.reservedCount);
                setCourseDate(result.data.courseDate);
                setCourse(result.data.course);
                setCancelableCount(result.data.cancelableCount);
            })
            .catch((error) => {
                console.error("상세 정보 가져오는 중 에러 발생:", error.response ? error.response.data : error.message);
                if (error.response) {
                    console.error("에러 상태 코드:", error.response.status);
                }
            });
    }, []);

    return (
        <div className="container">
            <header className="header">
                <div className="menu">
                    <h2>홈 &gt; 그룹예약 &gt; 예약상세</h2>
                    <div className="hamburger">☰</div>
                </div>
                <hr />
                <div className="user-info">
                    <p><strong>{memberName}({memberNum})</strong> 회원님</p>
                </div>
            </header>

            <div className="membership-box">
                <div className="left">
                    <p><strong>{subscriptionName}</strong></p>
                    <p>{startDate} ~ {endDate}</p>
                </div>
                <div className="right">
                    <p><strong>{completedCount} / {availableCount}</strong></p>
                    <p>(예약 {reservedCount}건)</p>
                </div>
            </div>

            <div>
                <h3>수업 시간</h3>
                <p>{courseDate}</p>
            </div>

            {
                course ?
                    <div className="tab-content" id="available">
                        <div className="time-group">
                            <div className="reservation-item">
                                <div className="left" style={{display: "flex", alignItems: "center"}}>
                                    <img alt="지수 프로필" style={{marginRight: "10px"}} />
                                    <div>
                                        <p><strong>{course.instructorName}({course.courseName})</strong></p>
                                        <p>{course.courseStartTime} - {course.courseEndTime}</p>
                                    </div>
                                </div>
                                <div className="right">
                                    <h2>{course.attendeeCount} / 6</h2>
                                </div>
                            </div>
                        </div>
                    </div> :
                    null
            }

            <div>

                <div className="reservation-details">
                    <h3>예약제한정보</h3>
                    <ul>
                        <li>예약횟수: 제한없음</li>
                        <li>취소횟수: 일 3회 중 {cancelableCount}회 취소 가능</li>
                        <li>예약시간: 항상 예약가능</li>
                        <li>취소시간: 당일 4시간 전까지만 취소가능</li>
                    </ul>
                </div>

                <div className="reservation-details" style={{marginBottom: "50px"}}>
                    <div className="comparison">
                        <p>비고</p>
                        <p>예약가능</p>
                    </div>
                </div>
            </div>

            <div className="reservation-footer">
                <div className="buttons">
                    <button className="close">닫기</button>
                    <button className="reserve">예약</button>
                </div>
            </div>

        </div>
    );
}

export default CourseDetail;