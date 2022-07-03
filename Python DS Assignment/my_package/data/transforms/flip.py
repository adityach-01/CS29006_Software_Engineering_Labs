#Imports
from PIL import ImageOps
from PIL import Image


class FlipImage(object):
    '''
        Flips the image.
    '''

    def __init__(self, flip_type='horizontal'):
        '''
            Arguments:
            flip_type: 'horizontal' or 'vertical' Default: 'horizontal'
        '''

        # Write your code here
        self.flip_type = flip_type

        
    def __call__(self, image):
        '''
            Arguments:
            image (numpy array or PIL image)

            Returns:
            image (numpy array or PIL image)
        '''
        if self.flip_type == 'horizontal':
            image_final = ImageOps.mirror(image)

        elif self.flip_type == "vertical":
            image_final = ImageOps.flip(image)

        return image_final

