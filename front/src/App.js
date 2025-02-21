import './App.css';
import { BrowserRouter as Routes, Route } from 'react-router-dom';
import CourseMain from './pages/course/CourseMain';
import CourseDetail from "./pages/course/CourseDetail";
import Login from "./pages/member/Login";
import RegisterCourse from "./pages/admin/RegisterCourse";
import RegisterInstructor from "./pages/admin/RegisterInstructor";

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/courses" element={<CourseMain></CourseMain>}></Route>
        <Route path="/courses/detail" element={<CourseDetail></CourseDetail>}></Route>

        <Route path="/members/login" element={<Login></Login>}></Route>

        <Route path="/admin/register/courses" element={<RegisterCourse></RegisterCourse>}></Route>
        <Route path="/admin/register/instructors" element={<RegisterInstructor></RegisterInstructor>}></Route>
      </Routes>
    </div>
  );
}

export default App;
