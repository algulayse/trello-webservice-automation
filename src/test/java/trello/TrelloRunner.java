package trello;

import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;
import trello.models.Board;
import trello.models.Card;
import trello.models.ToDoList;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class TrelloRunner extends TestBase {
    /*
    Web Servis Otomasyon
        - Proje Java programlama dilinde yazılmalıdır.
        - Proje Maven projesi olarak yazılmalıdır.
        - Rest-Asured kütüphanelerini kullanmanız gerekmektedir.
        - Projenizin GitHub üzerinden paylaşılması gerekmektedir
        - Projenizin OOP (Object Oriented Programming) prensiplerine uygun yazılması
        gerekmektedir.
        - Page Object Pattern kullanmanız gerekmektedir.
     Api Bilgileri
        - https://trello.com/app-key -> Key ve token bilgilerine ulaşabilirsiniz
        - https://developer.atlassian.com/cloud/trello/rest/ -> trello request lerin listesi.
        - Not: Token url ulaşmak için


    Senaryo Adımları
        - Trello üzerinde bir board oluşturunuz.
        - Oluşturduğunuz board’ a iki tane kart oluşturunuz.
        - Oluştrduğunuz bu iki karttan random olacak sekilde bir tanesini güncelleyiniz.
        - Oluşturduğunuz kartları siliniz.
        - Oluşturduğunuz board’ u siliniz
     */

    String idBoard = null;
    String idOfToDoList = null;
    String idCard = null;
    List<String> cardIds = new ArrayList<>();

    int deleteIndex;

    @Test(priority = 0)
    public void createANewBoard() {

        String boardName = "TestiniumBoard";
        String URL = URI + BOARD + "?name=" + boardName + "&" + AUTH;
        //System.out.println(URL);

        Board board = given()
                .contentType(ContentType.JSON)
                .post(URL)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(Board.class);

        idBoard = board.getId();

        //response.prettyPrint();

//        System.out.println();
//        System.out.println();
//        System.out.println("-------------------");
//        System.out.println(idBoard);
        Assert.assertEquals(board.getName(), boardName);
    }

    @Test(priority = 1)
    public void createAList() {
        String toDoListName = "TodoItemsForTestinium";
        String URL = URI + BOARD + idBoard + LIST + "?name=" + toDoListName + "&" + AUTH;
        // System.out.println(URL);
        //https://api.trello.com/1/boards/{id}/lists?name={name}
        //https://api.trello.com/1/boards/0vmgZQiY1/lists?name=items

        ToDoList toDoList = given().contentType(ContentType.JSON)
                .post(URL)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(ToDoList.class);

        idOfToDoList = toDoList.getId();

        Assert.assertEquals(toDoList.getClosed(), "false");
        Assert.assertEquals(toDoList.getName(), toDoListName);
    }

    @Test(priority = 2, invocationCount = 2)
    public void createANewCard() {
        String URL = URI + CARD + "?idList=" + idOfToDoList + "&" + AUTH;
        System.out.println(URL);
//        https://api.trello.com/1/cards?idList=5abbe4b7ddc1b351ef961414
        Card card = given().contentType(ContentType.JSON)
                .post(URL)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(Card.class);

        idCard = card.getId();
        cardIds.add(idCard);
    }

    @Test(priority = 3)
    public void updateACard() {
        String update = "HelloWorld!";
        int randomIndex = (int) Math.ceil(Math.random() * (cardIds.size() - 1));

        String URL = URI + CARD + "/" + cardIds.get(randomIndex) + "?desc=" + update + "&" + AUTH;

        Card updatedCard = given().contentType(ContentType.JSON)
                .put(URL)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(Card.class);

        Assert.assertEquals(updatedCard.getDesc(), update);

    }

    @Test(priority = 4, invocationCount = 2)
    public void deleteACard() {
        String URL = URI + CARD + "/" + cardIds.get(deleteIndex) + "?" + AUTH;
        deleteIndex++;
        System.out.println(URL);
        //https://api.trello.com/1/cards/{id}
        given().contentType(ContentType.JSON)
                .delete(URL)
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test(priority = 5)
    public void deleteABoard() {
        String url = "https://api.trello.com/1/boards/";
        String URL = url + idBoard + "?" + AUTH;
        given().contentType(ContentType.JSON)
                .delete(URL)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();

    }
}
