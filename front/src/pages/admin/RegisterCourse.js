import './RegisterCourse.css';
import {useEffect, useState} from "react";
import axios from "axios";


function RegisterCourse() {

    let [courseName, setCourseName] = useState("");
    let [instructorNames, setInstructorNames] = useState([]);
    let [selectedInstructorNames, setSelectedInstructorNames] = useState("");
    let dayOfWeeks = [["월", "MON"], ["화", "TUES"], ["수", "WED"], ["목", "THUR"], ["금", "FRI"], ["토", "SAT"]];
    let [selectedDayOfWeeks, setSelectedDayOfWeeks] = useState([]);
    let [startTime, setStarTime] = useState();
    let [endTime, setEndTime] = useState();

    useEffect(() => {
        axios.get("/instructors")
            .then((result) => {
                setInstructorNames(result.data);
            })
            .catch((error) => {
                console.error("강사 정보 가져오는 중 에러 발생:", error.response ? error.response.data : error.message);
                if (error.response) {
                    console.error("에러 상태 코드:", error.response.status);
                }
            });
    }, [])

    return (
        <div className="container">
            <div className="header">
                <div className="menu">
                    <h1>수업 등록</h1>
                    <span className="hamburger">☰</span>
                </div>
            </div>
            <hr />
            <div className="form-container">
                <h2>수업 등록</h2>
                <form>
                    <input type="text" placeholder="수업명" onChange={(e) => setCourseName(e.target.value)} required />
                        <select onChange={(e) => setSelectedInstructorNames(e.target.value)} required>
                            <option value="">강사명</option>
                            {
                                instructorNames.map(function(instructorName) {
                                    return (
                                        <option value={instructorName}>{instructorName}</option>
                                    );
                                })
                            }
                        </select>
                        <label>수업 요일:</label>
                        <div className="weekday-options">
                            {
                                dayOfWeeks.map(function(dayOfWeek) {
                                    return (
                                        <>
                                            <input type="checkbox" id={dayOfWeek[1]} value={dayOfWeek[0]} onClick={(e) => {
                                                if (e.target.checked) {
                                                    let copy = [...selectedDayOfWeeks];
                                                    copy.push(dayOfWeek[1]);
                                                    setSelectedDayOfWeeks(copy);
                                                } else {
                                                    let copy = [...selectedDayOfWeeks];
                                                    copy = copy.filter((v) => v !== dayOfWeek[1]);
                                                    setSelectedDayOfWeeks(copy);
                                                }
                                            }} />
                                            <label htmlFor={dayOfWeek[1]}>{dayOfWeek[0]}</label>
                                        </>
                                    );
                                })
                            }
                        </div>
                        <label>수업 시간:</label>
                        <div className="time-container">
                            <input type="time" id="start-time" onChange={(e) => setStarTime(e.target.value)} required /> ~
                            <input type="time" id="end-time" onChange={(e) => setEndTime(e.target.value)} required />
                        </div>
                        <button type="submit" onClick={(e) => {
                            e.preventDefault();

                            let result = window.confirm("수업을 등록하시겠습니까?");
                            if (!result) {
                                return;
                            }

                            axios.post("/admin/register/courses", {
                                    courseName: courseName,
                                    instructorName: selectedInstructorNames,
                                    dayOfWeeks: selectedDayOfWeeks,
                                    startTime: startTime,
                                    endTime: endTime
                                }, {
                                    headers: { "Content-Type": "application/json" }
                                })
                                .then((result) => {
                                    // TODO: 수업 조회 페이지 만든 후, 해당 페이지로 이동
                                })
                                .catch((error) => {
                                    console.error("수업 등록 중 에러 발생:", error.response ? error.response.data : error.message);
                                    if (error.response) {
                                        console.error("에러 상태 코드:", error.response.status);
                                    }
                                })
                        }}>등록</button>
                </form>
            </div>
        </div>
    );
}

export default RegisterCourse;