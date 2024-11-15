import { Alert, AlertIcon, Box, FormLabel, Input } from '@chakra-ui/react';
import { useField } from 'formik';
import React from 'react'

export default function AvatarInput({ label, ...props }) {
    const [field, meta, helpers] = useField(props);

  const handleChange = (event) => {
    const file = event.currentTarget.files[0];
    helpers.setValue(file); // Устанавливаем файл в Formik
  };
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Input type="file" onChange={handleChange} />
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"} mt={2}>
                    <AlertIcon />
                    {meta.error}
                </Alert>
            ) : null}
        </Box>
    );
}
