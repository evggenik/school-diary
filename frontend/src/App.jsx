import { useEffect, useState } from "react";
import Simple from "./components/shared/Navbar.jsx";
import { getTeachers } from "./services/client.js";
import { Spinner, Text } from "@chakra-ui/react";
import TeachersList from "./components/TeachersList.jsx";
import TeacherDetail from "./components/TeacherDetail.jsx";
import DrawerForm from "./components/DrawerForm.jsx";
//import OtherUsersCard from './OtherUsersCard'; // Импортируйте компонент для других пользователей

function App() {
  // const [showOtherUsers, setShowOtherUsers] = useState(false);
  const [showTeachers, setShowTeachers] = useState(false);
  const [teachers, setTeachers] = useState([]);
  const [selectedTeacher, setSelectedTeacher] = useState(null); // Состояние для выбранного учителя
  const [loading, setLoading] = useState(false);

  const fetchTeachers = () => {
    setLoading(true);
    getTeachers().then((res)=>{
      setTeachers(res.data);
    }).catch((err)=>{
      errorNotification(
        err.code,
        err.response.data.message
      )
    }).finally(()=>{
      setLoading(false);
    })
  }

  const handleBackToList = () => {
    setSelectedTeacher(null);
    setShowTeachers(true);
};

  useEffect(()=>{
    fetchTeachers();
  }, []);

  const handleSelectTeacher = (teacher) => {
    console.log("Selected teacher:", teacher);
    setSelectedTeacher(teacher); // Устанавливаем выбранного учителя
    setShowTeachers(false);
  };

  // const handleUpdateTeacher = (updatedTeacher) => {
  //   setTeachers(prev => prev.map(t => (t.id === updatedTeacher.id ? updatedTeacher : t)));
  // };

 

  if (loading) {
    return (
      <Simple setShowTeachers={setShowTeachers} setSelectedTeacher={setSelectedTeacher}>
        <Spinner/>
      </Simple>
    );
  }

  return (
    <Simple setShowTeachers={setShowTeachers} setSelectedTeacher={setSelectedTeacher}
    fetchTeachers={fetchTeachers}>
    {showTeachers ? (
        <TeachersList teachers={teachers} onSelectTeacher={handleSelectTeacher}
          fetchTeachers={fetchTeachers} />
        ) : (
          selectedTeacher &&
          <TeacherDetail teacher={selectedTeacher}
            fetchTeachers={fetchTeachers} onBackToList={handleBackToList} 
            // onUpdateTeacher={handleUpdateTeacher} 
            setSelectedTeacher={setSelectedTeacher}
          /> // Отображаем детали выбранного учителя, если он выбран
          )
    }
  </Simple>
  );
}

export default App
