import React, { useState } from 'react'
import TeachersList from './TeachersList';
import TeacherDetail from './TeacherDetail';

export default function TeachersContainer({ teachers }) {
    const [selectedTeacher, setSelectedTeacher] = useState(null);

    const handleSelectTeacher = (teacher) => {
      setSelectedTeacher(teacher);
    };
  
    return (
      <div>
        <TeachersList teachers={teachers} onSelectTeacher={handleSelectTeacher} />
        {selectedTeacher && <TeacherDetail teacher={selectedTeacher} />}
      </div>
    );
}
