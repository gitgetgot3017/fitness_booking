import './App.css';
import { Routes, Route } from 'react-router-dom';
import CourseMain from './pages/course/CourseMain';
import CourseDetail from "./pages/course/CourseDetail";
import Login from "./pages/member/Login";

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/courses" element={<CourseMain></CourseMain>}></Route>
        <Route path="/courses/detail" element={<CourseDetail></CourseDetail>}></Route>

        <Route path="/members/login" element={<Login></Login>}></Route>
      </Routes>
    </div>
  );
}

export default App;
