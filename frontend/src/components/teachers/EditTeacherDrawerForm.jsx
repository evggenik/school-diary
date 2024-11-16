import { CloseIcon, EditIcon } from '@chakra-ui/icons';
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
import EditTeacherForm from './EditTeacherForm';

export default function EditTeacherDrawerForm({ setSelectedTeacher,
                                              teacher }) {
    const { isOpen, onOpen, onClose } = useDisclosure()
  return <>

    <Button  
        leftIcon={<EditIcon />} 
        colorScheme='blue' 
        onClick={onOpen}
    >
        Edit
    </Button>
    <Drawer isOpen={isOpen} onClose={onClose} size={'xl'}>
          <DrawerOverlay />
          <DrawerContent>
            <DrawerCloseButton />
            <DrawerHeader>Edit teacher</DrawerHeader>
  
            <DrawerBody>
              <EditTeacherForm
                teacher={teacher}
            onClose={onClose}
            setSelectedTeacher={setSelectedTeacher}
              />
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