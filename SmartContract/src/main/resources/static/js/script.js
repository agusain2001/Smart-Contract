/**
 * 
 */
console.log("this is script file")

const toggleSidebar  =  () => {
	
	if($(".sidebar").is(":visible")){
		
		$(".sidebar").css("display","none");
		$(".content").css("margin-left","0%");
		
	}else{
		$(".sidebar").css("display","block");
		$(".content").css("margin-left","20%");
		
	}
	
};


const search = () => {
  let query = $("#search-input").val();

  if (query ==' ') {
    $(".search-result").hide();
  } else {

    let url = `http://localhost:8080/search/${query}`;

    fetch(url)
      .then((response) => {
        if (!response.ok) {
          throw new Error(response.statusText);
        }
        return response.json();
      })
      .then((data) => {
        console.log(data);

        let text = `<div class='list-group'>`;

        data.forEach((contact) => {
          text += `<a href='#' class='list-group-item list-group-action'>${contact.name}</a>`;
        });

        text += `</div>`;

        $(".search-result").html(text);
        $(".search-result").show();
      })
      .catch((error) => {
        console.error('Error:', error);
        // Handle the error here, e.g., display an error message to the user
      });
  }
};




