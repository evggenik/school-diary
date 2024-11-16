import { AddIcon, CloseIcon } from '@chakra-ui/icons';
import { 
    Button, 
    Drawer, 
    DrawerBody, 
    DrawerCloseButton, 
    DrawerContent, 
    DrawerFooter, 
    DrawerHeader, 
    DrawerOverlay,
    useDisclosure
} from '@chakra-ui/react';
import React from 'react';
import CreateTeacherForm from './CreateTeacherForm';

export default function DrawerForm({fetchTeachers}) {
    const { isOpen, onOpen, onClose } = useDisclosure()
  return <>

    <Button 
        flex='1' 
        variant={'outline'} 
        leftIcon={<AddIcon />} 
        colorScheme='blue' 
        onClick={onOpen}
    >
        New Teacher
    </Button>
    <Drawer isOpen={isOpen} onClose={onClose} size={'xl'}>
          <DrawerOverlay />
          <DrawerContent>
            <DrawerCloseButton />
            <DrawerHeader>Create new teacher</DrawerHeader>
  
            <DrawerBody>
          <CreateTeacherForm fetchTeachers={fetchTeachers} onClose={onClose}/>
            </DrawerBody>
  
            <DrawerFooter>
            <Button 

                variant={'outline'} 
                leftIcon={<CloseIcon />} 
                colorScheme='blue' 
                onClick={onClose}
    >
                Close
              </Button>
            </DrawerFooter>
          </DrawerContent>
        </Drawer>
    </>
}

