# import sys
# sys.path.append("..")
# from textbox import textBox
import pygame
from random import randint

def create_text_render(string):
    font= pygame.font.SysFont('sans',30)
    return font.render(string,True, WHITE)

def create_text_render_color(string,color):
    font= pygame.font.SysFont('sans',30)
    return font.render(string,True, color)
#covenrt str to int    
def convert_str(a_list):
    for i in range(len(a_list)):
        a_list[i]=int(a_list[i])
    return a_list

pygame.init()

screen = pygame.display.set_mode((1200,700))

pygame.display.set_caption("Kmeans Visualization")

running=True
clock = pygame.time.Clock()
BACKGROUND= (214,214,214)
BLACK= (0,0,0)
BACKGROUND_PANEL = (249,255,230)
WHITE = (255,255,255)

RED = (255,0,0)
GREEN = (0,255,0)
BLUE = (0,0,255)
YELLOW = (147,153,35)
PURPLE = (255,0,255)
SKY = (0,255,255)
ORANGE= (255,125,25)
GRAPE = (100,25,125)
GRASS = (55,155,65)

COLORS = [RED,GREEN,BLUE,YELLOW,PURPLE,SKY,ORANGE,GRAPE,GRASS]

#font small
font_small = pygame.font.SysFont('sans',15)

text_plus = create_text_render('+')
text_minus = create_text_render('-')
text_run = create_text_render('Run')
text_algorithm = create_text_render('Algorithm')
text_random = create_text_render('Random')
text_reset = create_text_render('Reset')
text_input = create_text_render('Input')

K=0 
Error=0
points = []
clusters = []
validChars = "1234567890" 
class TextBox(pygame.sprite.Sprite):
    pygame.init()
 
    def __init__(self):
        self.screen = pygame.display.set_mode([400, 100])
        pygame.sprite.Sprite.__init__(self)
        self.text = ""
        self.font = pygame.font.SysFont('sans', 20)
        self.image = self.font.render("Enter coordinate", False, [0, 0, 0])
        self.rect = self.image.get_rect()

    def add_chr(self, char):
        if char in validChars:
            self.text += char
        self.update()

    def update(self):
        old_rect_pos = self.rect.center
        self.image = self.font.render(self.text, False, [0, 0, 0])
        self.rect = self.image.get_rect()
        self.rect.center = old_rect_pos
        
    def program(self):
        self.rect.center = [200,50]
        running=True
        while running:
            self.screen.fill([255, 255, 255])
            self.screen.blit(self.image, self.rect)
            pygame.display.flip()
            for e in pygame.event.get():
                if e.type == pygame.QUIT:
                    running = False
                if e.type == pygame.KEYDOWN:
                    self.add_chr(pygame.key.name(e.key))
                    if e.key == pygame.K_SPACE:
                        self.text += " "
                        self.update()
                    if e.key == pygame.K_BACKSPACE:
                        self.text = self.text[:-1]
                        self.update()
                    if e.key == pygame.K_RETURN:
                        if len(self.text) > 0:
                            print (self.text)
                            sub_string = self.text.split()
                            points.append(convert_str(sub_string))
                            running = False    

while running:
    clock.tick(60)
    screen.fill(BACKGROUND)
    
    #Draw interace
    #Draw panel

    pygame.draw.rect(screen, BLACK, (50,50,700,500))
    pygame.draw.rect(screen, BACKGROUND_PANEL, (55,55,690,490))

    #K button +
    pygame.draw.rect(screen, BLACK, (850,50,50,50))
    screen.blit(text_plus, (860,50))

    #K button -
    pygame.draw.rect(screen, BLACK, (940,50,50,50))
    screen.blit(text_minus, (960,50))

    #K Value
    text_k = create_text_render_color('K = '+ str(K),BLACK)
    screen.blit(text_k, (870,4))

    #Run button
    pygame.draw.rect(screen, BLACK, (850,150,150,50))
    screen.blit(text_run, (900,150))

    #Algorithm button 
    pygame.draw.rect(screen, BLACK, (850,350,150,50))
    screen.blit(text_algorithm, (850,350))

    #Random button 
    pygame.draw.rect(screen, BLACK, (850,250,150,50))
    screen.blit(text_random, (850,250))

    #Reset button 
    pygame.draw.rect(screen, BLACK, (850,450,150,50))
    screen.blit(text_reset, (875,450))

    #Error
    text_error = create_text_render_color('Error = '+str(Error),BLACK)
    screen.blit(text_error, (860,300))

    # Input button
    pygame.draw.rect(screen, BLACK, (1020,150,150,50))
    screen.blit(text_input, (1030,150))
    
    #Define mouse pos
    mouse_x, mouse_y = pygame.mouse.get_pos()  

    #Draw mouse position when mouse is in panel
    if 50 < mouse_x < 750 and 50 < mouse_y < 550:
        text_mouse = font_small.render("(" +str(mouse_x-50) + "," + str(mouse_y-50) + ")", True ,BLACK)
        screen.blit(text_mouse, (mouse_x + 10, mouse_y))


   

    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False
        if event.type == pygame.MOUSEBUTTONDOWN:
            #Create point on panel
            if 50 < mouse_x < 750 and 50 < mouse_y < 550:
                point= [mouse_x-50,mouse_y-50]
                points.append(point)
                print(points)
            #Change K button +
            if 850 < mouse_x < 900 and 50 < mouse_y < 100:
                if K<len(COLORS):
                    K= K+1
                print('Press K+')
            #Change K button -
            if 950 < mouse_x < 1000 and 50 < mouse_y < 100:
                if K>0:
                    K= K-1
                print('Press K-')
            # Run button
            if 850 < mouse_x < 1000 and 150 < mouse_y < 200:
                print('Run pressed')
            # Random button
            if 850 < mouse_x < 1000 and 250 < mouse_y < 300:
                clusters= []
                for i in range(K):
                    random_point = [randint(0,700),randint(0,500)]
                    clusters.append(random_point)
                print('Random pressed')
            # Algorithm button
            if 850 < mouse_x < 1000 and 350 < mouse_y < 400:
                print('Algorithm pressed')
            # Reset button
            if 850 < mouse_x < 1000 and 450 < mouse_y < 500:
                print('Reset pressed')
            # Input button
            if 1030 < mouse_x < 1180 and 150 < mouse_y < 200:
                textbox=TextBox()
                textbox.program()
                screen = pygame.display.set_mode((1200,700))




    #Draw cluster
    for i in range(len(clusters)):
        pygame.draw.circle(screen, COLORS[i], (clusters[i][0]+55, clusters[i][1]+55), 10)
    #Draw point
    for i in range(len(points)):
        pygame.draw.circle(screen, BLACK, (points[i][0] + 50, points[i][1] + 50), 6)
        pygame.draw.circle(screen, WHITE, (points[i][0] + 50, points[i][1] + 50), 5)

    pygame.display.flip()

pygame.quit()