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
    input, answer = load_data("TrainingData.csv")
    model = build_model()
    model.fit(input, answer, epochs=130, batch_size=10)

    _, accuracy = model.evaluate(input, answer)
    print('Accuracy: %.2f' % (accuracy*100))

    Button(root, text="Quit", command=root.quit).pack()
    root.after(0, place_circle)
    root.mainloop()


def display_answer(rgb, answer):
    global circleX
    if answer == 0:
        canvas.move(circle, 200-circleX, 0)
        circleX = 200
    elif answer == 1:
        canvas.move(circle, 300 - circleX, 0)
        circleX = 300
    canvas.configure(bg="#%02x%02x%02x" % rgb)
    canvas.update()


def place_circle():
    global state_bubble
    display_answer((random.randint(0, 255), random.randint(0, 255), random.randint(0, 255)), state_bubble)
    state_bubble = 0 if state_bubble == 1 else 1
    root.after(1000, place_circle)


def load_data(file_name):
    initial_data = numpy.genfromtxt(file_name, delimiter=',')
    training_input = initial_data[:, 0:3]
    traning_answer = initial_data[:, 3]
    return training_input, traning_answer  # 0 is input, 1 is answer


def build_model():
    model = Sequential()
    model.add(Dense(12, input_dim=3, activation='relu'))
    model.add(Dense(8,activation='relu'))
    model.add(Dense(1, activation='sigmoid'))

    model.compile(loss='binary_crossentropy', optimizer='adam', metrics=['accuracy'])
    return model


if __name__ == "__main__":
    main()
