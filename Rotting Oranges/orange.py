import turtle
import copy


def rotting_oranges(arr):
    arr = copy.deepcopy(arr)
    row = len(arr)
    column = len(arr[0])

    rst = [copy.deepcopy(arr)]  # copy the given list to be the initial result
    infecting = True
    while infecting:
        infecting = False
        for i in range(row):
            for j in range(column):
                if arr[i][j] == 2:
                    # mark out those oranges which are going to be infected
                    if i - 1 >= 0 and arr[i - 1][j] == 1:
                        arr[i - 1][j] = 3
                        infecting = True
                    if i + 1 < row and arr[i + 1][j] == 1:
                        arr[i + 1][j] = 3
                        infecting = True
                    if j - 1 >= 0 and arr[i][j - 1] == 1:
                        arr[i][j - 1] = 3
                        infecting = True
                    if j + 1 < column and arr[i][j + 1] == 1:
                        arr[i][j + 1] = 3
                        infecting = True
        rst.append(copy.deepcopy(arr))  # don't use rst.append(arr)
        for i in range(row):
            for j in range(column):
                if arr[i][j] == 3:
                    arr[i][j] = 2
        rst.append(copy.deepcopy(arr))  # don't use rst.append(arr)
    return rst


def draw_grid(t, x, y, size, color):
    """Draw a single grid"""
    t._tracer(0)  # turn off screen updates because we only care about the finished pic
    t.color(color)
    t.pensize(3)
    t.setheading(0)
    t.penup()
    t.goto(x, y)
    t.pendown()
    for i in range(4):
        t.forward(size)
        t.right(90)
    t.penup()


def draw_orange(t, x, y, big_r, small_r, infected=False, rotten=False):
    """
    Draw an orange, the status could be: fresh, infected or rotten
    big_r and small_r define the shape and size of the orange
    https://pythonturtle.academy/tutorial-drawing-an-oval-with-python-turtle/
    """
    t._tracer(0)
    if rotten:
        t.color("DarkOrange4")
    else:
        t.color("orange")
    t.up()
    t.goto(x, y)
    t.down()
    t.seth(-45)
    t.begin_fill()
    t.circle(big_r, 90)
    t.circle(small_r, 90)
    t.circle(big_r, 90)
    t.circle(small_r, 90)
    t.end_fill()

    if infected:
        t.color("WhiteSmoke")
        t.up()
        t.goto(x + 70, y + 22)
        t.down()
        t.seth(85)
        t.begin_fill()
        t.circle(25, 90)
        t.circle(18, 90)
        t.circle(25, 90)
        t.circle(18, 90)
        t.end_fill()

        t.color("DarkSeaGreen")
        t.up()
        t.goto(x + 65, y + 25)
        t.down()
        t.seth(85)
        t.begin_fill()
        t.circle(20, 90)
        t.circle(13, 90)
        t.circle(20, 90)
        t.circle(13, 90)
        t.end_fill()

    if rotten:
        t.color("DarkOrange3")
        t.up()
        t.goto(x + 60, y + 35)
        t.down()
        t.seth(110)
        t.begin_fill()
        t.circle(15, 90)
        t.circle(10, 90)
        t.circle(20, 90)
        t.circle(10, 90)
        t.end_fill()

    t.pensize(5)
    t.penup()
    t.goto(x + 35, y + 50)
    t.pendown()
    if rotten:
        t.color("DimGrey")
        t.seth(70)
        t.forward(10)
    else:
        t.color("DarkGreen")
        t.seth(90)
        t.forward(10)
        t.right(70)
        t.forward(8)
    t.penup()


class RottingOranges(object):

    def __init__(self, data):
        # create a screen
        self.screen = turtle.Screen()
        self.screen.bgcolor("ivory")
        self.screen.screensize(600, 600)

        # write the title of the window
        title = turtle.Turtle(visible=False)
        title.penup()
        title.setpos(-10, 250)
        title.color("DarkOrange")
        title.pendown()
        title.write("Rotting Oranges", align="center", font=("Arial", 30, "bold"))

        # draw the board with given data
        self.data = data
        self.board = turtle.Turtle(visible=False)
        self.draw_board()

        # draw the oranges on Day1
        self.orange = turtle.Turtle(visible=False)
        self.textbox = turtle.Turtle(visible=False)
        self.draw_picture(self.data, 1)

        # create a blue cursor
        self.cursor = turtle.Turtle(visible=False)
        self.cursor_position = [-216, -128]  # same with the starting point of draw_board
        draw_grid(self.cursor, self.cursor_position[0], self.cursor_position[1], 100, "blue")

        # create a selecting tool
        self.select_tool = turtle.Turtle(visible=False)
        self.point_list = []  # the list records the positions of selected oranges (max_length = 2)

        self.input_list = rotting_oranges(self.data)
        self.current_index = 0  # the initial status is Day1

        turtle.listen()

        turtle.onkey(self.move_up, "Up")
        turtle.onkey(self.move_down, "Down")
        turtle.onkey(self.move_left, "Left")
        turtle.onkey(self.move_right, "Right")
        turtle.onkey(self.select, "Return")  # confirm to select an orange by pressing "Enter"
        turtle.onkey(self.start, "Y")  # start to show the changing process by pressing "Y"
        turtle.onkey(self.show_next, "N")  # show the next status by pressing "N"
        turtle.onkey(self.show_previous, "P")  # show the previous status by pressing "P"

        self.screen.mainloop()  # Starts event loop, this must be the last statement in a turtle program

    def draw_board(self):
        """this method draws the grey board"""
        row = len(self.data)
        column = len(self.data[0])

        start_x = -216  # starting x position of the grid
        start_y = -128  # starting y position of the grid

        for i in range(row):
            for j in range(column):
                draw_grid(self.board, start_x + j * 100, start_y + i * 100, 100, "grey72")

    def draw_picture(self, data, day):
        """this method draws the oranges with given data and print out the textbox"""
        data = data[::-1]  # we draw from bottom to top but display from top to bottom
        row = len(data)
        column = len(data[0])

        start_x = -200  # starting x position of the oranges
        start_y = -200  # starting y position of the oranges

        for i in range(row):
            for j in range(column):
                if data[i][j] == 1:
                    draw_orange(self.orange, start_x + j * 100, start_y + i * 100, 50, 30)
                elif data[i][j] == 2:
                    draw_orange(self.orange, start_x + j * 100, start_y + i * 100, 50, 30, rotten=True)
                elif data[i][j] == 3:
                    draw_orange(self.orange, start_x + j * 100, start_y + i * 100, 50, 30, infected=True)

        day = int(day)
        self.textbox.penup()  # create a textbox at the top right corner
        self.textbox.setpos(170, 210)
        self.textbox.color("grey72")
        self.textbox.pendown()
        pen = self.textbox.getpen()  # use getpen() to write so it can be removed later
        pen.write(f"Day {day}", font=("Arial", 24, "italic"))

    def move_up(self):
        row = len(self.data)
        if self.cursor_position[1] + 100 >= -128 + row * 100:
            return  # if the cursor has already reached the boundary, do nothing
        self.cursor.clear()
        self.cursor_position[1] += 100
        draw_grid(self.cursor, self.cursor_position[0], self.cursor_position[1], 100, "blue")
        self.screen.update()

    def move_down(self):
        if self.cursor_position[1] <= -128:
            return  # if the cursor has already reached the boundary, do nothing
        self.cursor.clear()
        self.cursor_position[1] -= 100
        draw_grid(self.cursor, self.cursor_position[0], self.cursor_position[1], 100, "blue")
        self.screen.update()

    def move_left(self):
        if self.cursor_position[0] <= -216:
            return  # if the cursor has already reached the boundary, do nothing
        self.cursor.clear()
        self.cursor_position[0] -= 100
        draw_grid(self.cursor, self.cursor_position[0], self.cursor_position[1], 100, "blue")
        self.screen.update()

    def move_right(self):
        column = len(self.data[0])
        if self.cursor_position[0] + 100 >= -216 + column * 100:
            return  # if the cursor has already reached the boundary, do nothing
        self.cursor.clear()
        self.cursor_position[0] += 100
        draw_grid(self.cursor, self.cursor_position[0], self.cursor_position[1], 100, "blue")
        self.screen.update()

    def select(self):
        """Confirm the selected orange by pressing the Enter key
        The cursor should turn from blue to red"""
        if len(self.point_list) == 2:
            return  # do not accept new points if there are already two points
        draw_grid(self.select_tool, self.cursor_position[0], self.cursor_position[1], 100, "red")
        self.screen.update()
        row = len(self.data)
        c = (self.cursor_position[0] + 216) // 100
        r = (row - 1) - (self.cursor_position[1] + 128) // 100
        self.point_list.append((r, c))

    def start(self):
        """
        Clear the cursors, get the positions of the selected oranges
        Show the updated orange status after swapping the two selected oranges
        """
        self.select_tool.clear()
        self.cursor.clear()

        if len(self.point_list) == 2:
            A = self.point_list[0]  # get the position of the first orange
            B = self.point_list[1]  # get the position of the second orange
            # swap the status value of orange A and orange B
            self.data[A[0]][A[1]], self.data[B[0]][B[1]] = self.data[B[0]][B[1]], self.data[A[0]][A[1]]
            self.orange.clear()
            self.draw_picture(self.data, 1)  # redraw the oranges after swapping, day = 1
            self.input_list = rotting_oranges(self.data)
        self.screen.update()

    def show_next(self):
        if self.current_index >= len(self.input_list) - 1:
            return  # if we have reached the end of the input_list, do nothing

        self.current_index += 1
        day = self.current_index // 2 + 1
        self.orange.clear()
        self.textbox.clear()
        self.draw_picture(self.input_list[self.current_index], day)
        self.screen.update()

    def show_previous(self):
        if self.current_index <= 0:
            return  # if we have arrived at the initial day, do nothing

        self.current_index -= 1
        day = self.current_index // 2 + 1
        self.orange.clear()
        self.textbox.clear()
        self.draw_picture(self.input_list[self.current_index], day)
        self.screen.update()


def main():
    my_data = [[0, 1, 0, 1], [2, 1, 0, 1], [0, 1, 1, 1]]
    RottingOranges(my_data)

if __name__ == '__main__':
    main()
