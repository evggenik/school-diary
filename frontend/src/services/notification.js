import { createStandaloneToast } from '@chakra-ui/react'

const { toast } = createStandaloneToast()

const notification = (title, description, status) => {
    toast({
        title,
        description,
        status,
        isClosable: true,
        duration: 5000
    })
}

export const successNotification = (title, description, status) => {
    notification(
        title,
        description,
        "success"
    )
}

export const errorNotification = (title, description, status) => {
    notification(
        title,
        description,
        "error"
    )
}