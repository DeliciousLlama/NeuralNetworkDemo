from datetime import time
from tkinter import *
import time
import numpy
from keras.layers import Input
from keras.models import Model, Sequential
from keras.layers.core import Dense, Dropout
from keras.layers.normalization import BatchNormalization
from keras.layers.advanced_activations import LeakyReLU
from keras.optimizers import Adam
from keras import initializers
from tqdm import tqdm
import os
import random

os.environ["KERAS_BACKEND"] = "tensorflow"

root = Tk()
canvas = Canvas(root, width=500, height=300)
canvas.pack()
tW = canvas.create_text(300, 250, text="Hello123", justify='center', fill='white')
tB = canvas.create_text(200, 250, text="Hello123", justify='center', fill='black')
circleX = 250
circle = canvas.create_oval(circleX-5, 230-5, circleX+5, 230+5, fill='white', width=1)
state_bubble = 0


def main():
    Button(root, text="Quit", command=root.quit).pack()

    root.after(0, place_circle)
    root.mainloop()

    # data = numpy.genfromtxt('TrainingData.csv', delimiter=',')


def displayThings(rgb, answer):
    global circleX
    if answer == 0:
        canvas.move(circle, 200-circleX,0)
        circleX = 200
    elif answer == 1:
        canvas.move(circle, 300 - circleX, 0)
        circleX = 300
    canvas.configure(bg="#%02x%02x%02x" % rgb)
    canvas.update()


def place_circle():
    global state_bubble
    displayThings((random.randint(0, 255), random.randint(0, 255), random.randint(0, 255)), state_bubble)
    state_bubble = 0 if state_bubble == 1 else 1
    root.after(1000, place_circle)


if __name__ == "__main__":
    main()
