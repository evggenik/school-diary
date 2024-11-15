import axios from "axios";

export const getTeachers = (async () => {
    try {
        return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/teachers`)
    } catch (e) {
        throw e;
    }
});

export const fetchTeacherById = async (id) => {
    try {
        const response = await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/teachers/${id}`);
        return response.data; // Возвращаем данные пользователя
    } catch (error) {
        throw error; // Обрабатываем ошибку
    }
};


export const newTeacher = async (teacher) => {
    try {
        const formData = new FormData();

           // Добавляем объект teacher как строку JSON
        formData.append('newTeacherDto',
            new Blob([JSON.stringify(teacher)], { type: 'application/json' }));
        
        // Проверяем, есть ли файл аватара и добавляем его
        if (teacher.avatarFile) {
            formData.append('avatarFile', teacher.avatarFile);
        }

        return await axios.post(
            `${import.meta.env.VITE_API_BASE_URL}/api/v1/teachers`,
            formData
        );
    } catch (e) {
        throw e;
    }
}

export const deleteTeacher = async (teacherId) => {
    try {
        return await axios.delete(`${import.meta.env.VITE_API_BASE_URL}/api/v1/teachers/${teacherId}`)
    } catch (e) {
        throw e;
    }
};

export const updateTeacher = async (teacher) => {
    try {
        const formData = new FormData();

           // Добавляем объект teacher как строку JSON
        formData.append('editTeacherDto',
            new Blob([JSON.stringify(teacher)], { type: 'application/json' }));
        
        // Проверяем, есть ли файл аватара и добавляем его
        if (teacher.avatarFile) {
            formData.append('avatarFile', teacher.avatarFile);
        }

        return await axios.put(
            `${import.meta.env.VITE_API_BASE_URL}/api/v1/teachers/${teacher.id}`,
            formData
        );
    } catch (e) {
        throw e;
    }
}