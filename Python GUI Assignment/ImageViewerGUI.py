####### REQUIRED IMPORTS FROM THE PREVIOUS ASSIGNMENT #######
from my_package.model import InstanceSegmentationModel
from my_package.data import Dataset
from my_package.analysis import plot_visualization
from my_package.data.transforms import FlipImage, RescaleImage, BlurImage, CropImage, RotateImage
import numpy as np

####### ADD THE ADDITIONAL IMPORTS FOR THIS ASSIGNMENT HERE #######
from tkinter import *
from tkinter import filedialog
from PIL import Image, ImageTk
from functools import partial

# Define the function you want to call when the filebrowser button is clicked.

def fileClick(clicked,trform):

    global img_box_f 
    global img_mask_f 

    filename = filedialog.askopenfilename(initialdir="C:/Users/91950/OneDrive/Desktop/Python DS Assignment/20CS10005_Aditya Choudhary_Python_DS_Assignment",title="Select an Image", filetypes=(("jpg files", "*.jpg"),("png files","*.png"), ("all files", "*.*")))
    
    file_name.set(filename)

    if(file_name.get() == ""):
        print("Please select an image first")
        return

    s = file_name.get()

    img_name = s.rsplit('/')[-1]
    num = (int)(img_name.rsplit('.')[0])
    # File name processing done

    transforms = []

    if trform.get() == "Blur 2":
        transforms.append(BlurImage(2))
    
    elif trform.get() == "Blur 4":
        transforms.append(BlurImage(4))
        
    elif trform.get() == "Rotate 45":
        transforms.append(RotateImage(45))
       
    elif trform.get() == "Rotate 90":
        transforms.append(RotateImage(90))
    
    elif trform.get() == "Flip":
        transforms.append(FlipImage())

    # Instantiate the segmentor model.
    segmentor = InstanceSegmentationModel()
    # Instantiate the dataset.
    annotation_file = './data/annotations.jsonl'
    dataset = Dataset(annotation_file, transforms=transforms)


    image_n = dataset[num]['image']
    PredictionBoxes, PredictionMasks, PredictionClass, PredictionScore = segmentor(image_n)
    image_n = np.transpose(image_n,(1,2,0))
    # Restoring the shape of the image

    # plot visualtsation returns the numpy array versions of the images
    img_box,img_mask = plot_visualization(image_n,PredictionBoxes, PredictionMasks, PredictionClass, PredictionScore)
  
    # converting numpy array to the images
    img_box_f = Image.fromarray(np.uint8(img_box*255))
    img_mask_f = Image.fromarray(np.uint8(img_mask*255))

    #resizing the images 
    img_box_f = img_box_f.resize((400, 400))
    img_box_f = ImageTk.PhotoImage(img_box_f)

    img_mask_f = img_mask_f.resize((400, 400))
    img_mask_f = ImageTk.PhotoImage(img_mask_f)
    

#defining the function that displays the image and its corresponding transformation
def process(clicked):

    global img_box_f
    global img_mask_f

    img=Image.open(file_name.get())

    img=img.resize((400, 400))
    img = ImageTk.PhotoImage(img)

    panel1 = Label(root, image = img)

    if clicked.get() == "Bounding-box":
        panel2 = Label(root, image = img_box_f)
        panel2.photo = img_box_f
    else:
        panel2 = Label(root, image = img_mask_f)
        panel2.photo = img_mask_f
    
    panel1.photo = img
    l1 = Label(text = "Original Image",font=("Arial",20))
    l1.grid(row=1,column=0,columnspan=2,pady=20)

    l2 = Label(text = clicked.get(),font=("Arial",20))
    l2.grid(row=1,column=2,columnspan=2,pady= 20)

    panel1.grid(row=2, column=0,columnspan=2,padx=0,pady=0)
    panel2.grid(row=2, column=2,columnspan=2,padx=0,pady=0)

	
if __name__ == '__main__':

    root = Tk()
    root.title("Image Model")
    
    # Declare the options.
    options = ["Segmentation", "Bounding-box"]
    clicked = StringVar()
    clicked.set(options[0])

    file_name=StringVar()
    file_name.set("")

    efile = Entry(root,textvariable=file_name, width=70)
    efile.grid(row=0, column=0,padx=5)
    
    transforms = ["None","Blur 2","Blur 4","Rotate 45","Rotate 90","Flip"]
    trform = StringVar()
    trform.set("Transformations")
    trans_button = OptionMenu(root,trform,*transforms)
    trans_button.grid(row=0,column=3,padx=5)


    fileButton=Button(root, text="Choose Image", command=partial(fileClick,clicked,trform))
    fileButton.grid(row=0, column=1,padx=20)

    dropDown=OptionMenu(root,clicked,*options)
    dropDown.grid(row=0,column=2,padx=5)
 
    # This is a `Process` button, check out the sample video to know about its functionality
    myButton = Button(root, text="Process", command=partial(process,clicked))
    myButton.grid(row=0, column=5,padx=5)


    # Execute with mainloop()
    root.mainloop()
# C:/Users/91950/OneDrive/Desktop/Python DS Assignment/20CS10005_Aditya Choudhary_Python_DS_Assignment/data/imgs/1.jpg
  