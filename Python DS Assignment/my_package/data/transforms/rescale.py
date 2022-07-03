#Imports
from PIL import Image


class RescaleImage(object):
    '''
        Rescales the image to a given size.
    '''

    def __init__(self, output_size):
        '''
            Arguments:
            output_size (tuple or int): Desired output size. If tuple, output is
            matched to output_size. If int, smaller of image edges is matched
            to output_size keeping aspect ratio the same.
        '''
        self.output = output_size
        # If tuple , the resizing is done considering the input is (w,h)

    def __call__(self, image):
        '''
            Arguments:
            image (numpy array or PIL image)

            Returns:
            image (numpy array or PIL image)

            Note: You do not need to resize the bounding boxes. ONLY RESIZE THE IMAGE.
        '''

        # Write your code here
        if type(self.output) is tuple:
            newsize = self.output
        
        elif type(self.output) is int:
            x,y = image.size
            if x > y:
                y = self.output
            else:
                x = self.output
            newsize = (x,y)
        
        image_final = image.resize(newsize)

        return image_final

  
       