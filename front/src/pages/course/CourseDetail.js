import './CourseDetail.css';
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import api from "../../api";

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
    let [errorState, setErrorState] = useState({
        'enrollmentLimitExceeded': false,
        'duplicateEnrollment': false,
        'classCapacityExceeded': false,
        'reservationCapacityExceeded': false,
        'cancellationLimitExceeded': false,
        'exceeded4HourLimit': false,
        'courseExpiration': false,
        'alreadyWaitCourse': false
    });

    let navigate = useNavigate();

    let params = new URLSearchParams();
    params.append("date", window.localStorage.getItem("courseDate"));
    params.append("courseId", window.localStorage.getItem("courseId"));

    useEffect(() => {
        api.get("/courses/detail", { params })
            .then((result) => {
                setMemberName(result.data.courseDetailInfo.memberName);
                setMemberNum(result.data.courseDetailInfo.memberNum);
                setSubscriptionName(result.data.courseDetailInfo.subscriptionName);
                setStartDate(result.data.courseDetailInfo.startDate);
                setEndDate(result.data.courseDetailInfo.endDate);
                setCompletedCount(result.data.courseDetailInfo.completedCount);
                setAvailableCount(result.data.courseDetailInfo.availableCount);
                setReservedCount(result.data.courseDetailInfo.reservedCount);
                setCourseDate(result.data.courseDetailInfo.courseDate);
                setCourse(result.data.courseDetailInfo.course);
                setCancelableCount(result.data.courseDetailInfo.cancelableCount);

                setErrorState({
                    enrollmentLimitExceeded: result.data.errorResponse.enrollmentLimitExceeded,
                    duplicateEnrollment: result.data.errorResponse.duplicateEnrollment,
                    classCapacityExceeded: result.data.errorResponse.classCapacityExceeded,
                    reservationCapacityExceeded: result.data.errorResponse.reservationCapacityExceeded,
                    cancellationLimitExceeded: result.data.errorResponse.cancellationLimitExceeded,
                    exceeded4HourLimit: result.data.errorResponse.exceeded4HourLimit,
                    courseExpiration: result.data.errorResponse.courseExpiration,
                    alreadyWaitCourse: result.data.errorResponse.alreadyWaitCourse
                });
            })
            .catch((error) => {
                console.error("상세 정보 가져오는 중 에러 발생:", error.response ? error.response.data : error.message);
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
                                    <img src={course.instructorImgUrl} style={{marginRight: "10px", width: "60px", height: "60px"}} />
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
                    {
                        errorState.duplicateEnrollment ?
                            ( // 해당 수업을 이미 신청한 경우
                                errorState.cancellationLimitExceeded ?
                                    null : // 이미 취소를 3번 한 경우
                                    (
                                        errorState.exceeded4HourLimit ?
                                            null : // 수업 4시간 전이 아닌 경우
                                            <button className="reserve" onClick={() => {
                                                alert("수강 취소하시겠습니까?");

                                                api.post("/courses/cancellation", {
                                                        date: window.localStorage.getItem("courseDate"),
                                                        courseId: window.localStorage.getItem("courseId")
                                                    }, {
                                                        headers: { "Content-Type": "application/json" }
                                                    })
                                                    .then(() => {
                                                        alert("수강 취소하였습니다.");
                                                        navigate("/courses");
                                                    })
                                                    .catch((error) => {
                                                        console.error("수강 취소 중 에러 발생:", error.response ? error.response.data : error.message);
                                                    });
                                            }}>수강 취소</button>
                                    )
                            ) :
                            ( // 해당 수업을 신청하지 않은 경우
                                errorState.enrollmentLimitExceeded || errorState.courseExpiration ?
                                    null : // 하루 최대 수강 횟수를 다 채운 경우 또는 전체 수강 횟수를 다 채운 경우
                                    (
                                        errorState.classCapacityExceeded ?
                                            ( // 수강 인원이 다 찬 경우
                                                errorState.reservationCapacityExceeded ? // 대기 인원이 다 찬 경우
                                                    null :
                                                    (
                                                        errorState.alreadyWaitCourse ? // 대기 신청을 한 경우
                                                            <button className="reserve" onClick={() => {
                                                                alert("알림 신청을 취소하시겠습니까?");

                                                                api.delete("/courses/notifications/cancellation", {
                                                                        date: window.localStorage.getItem("courseDate"),
                                                                        courseId: window.localStorage.getItem("courseId")
                                                                    }, {
                                                                        headers: { "Content-Type": "application/json" }
                                                                    })
                                                                    .then(() => {
                                                                        alert("수강 취소하였습니다.");
                                                                        navigate("/courses");
                                                                    })
                                                                    .catch((error) => {
                                                                        console.error("수강 취소 중 에러 발생:", error.response ? error.response.data : error.message);
                                                                    });
                                                            }}>알림 취소</button> :
                                                            <button className="reserve" onClick={() => {
                                                                api.post("/courses/notifications", {
                                                                        date: window.localStorage.getItem("courseDate"),
                                                                        courseId: window.localStorage.getItem("courseId")
                                                                    }, {
                                                                        headers: { "Content-Type": "application/json" }
                                                                    })
                                                                    .then(() => {
                                                                        alert("대기 신청하였습니다.");
                                                                        navigate("/courses");
                                                                    })
                                                                    .catch((error) => {
                                                                        console.error("대기 신청 중 에러 발생:", error.response ? error.response.data : error.message);
                                                                    });
                                                            }}>알림 신청</button>
                                                    )
                                            ) :
                                            <button className="reserve" onClick={() => {
                                                api.post("/courses/reservations", {
                                                        date: window.localStorage.getItem("courseDate"),
                                                        courseId: window.localStorage.getItem("courseId")
                                                    }, {
                                                        headers: { "Content-Type": "application/json" }
                                                    })
                                                    .then(() => {
                                                        alert("수강 예약하였습니다.");
                                                        navigate("/courses");
                                                    })
                                                    .catch((error) => {
                                                        console.error("수강 예약 중 에러 발생:", error.response ? error.response.data : error.message);
                                                    });
                                            }}>예약</button>
                                    )
                            )
                    }
                </div>
            </div>

        </div>
    );
}

export default CourseDetail;