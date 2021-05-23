PLAGIARISM STATEMENT:
I pledge the highest level of ethical principles in support of academic excellence.
I ensure that all of my work reflects my own abilities and not those of someone else.

ANSWERS TO HYPOTHETICAL QUESTIONS:
I would set an onClickListener for the TextView on each holder to hide the TextView and
display an EditView in the same place with the same text. I would also force the keyboard to open
in this listener. Upon clicking somewhere else (can be done with OnFocusChangeListener) I
would hide the EditText and display the TextView with the new text.
I think it would be pretty easy to figure out this flow because it's quite common for text
to be editable upon touch and it's a person first instinct to touch the part of the app they
want to change.
This flow is very consistent with others for editing text in android since it's a very common
flow in many apps and it doesn't require adding any buttons (for example Google Keep uses this
flow for its todo lists).
Implementing this wouldn't be very difficult since it requires only doing the following:
* Adding a setDescription function to the TodoList
* Adding an EditText object to TodoItemHolder and to row_todo_item.xml which starts hidden
  (same place as the TextView).
* In the adapter's function 'onBindViewHolder' I would add an onClickListener which would
  hide the TextView and show the EditText with TextView's text.
* In the same function I would add a OnFocusChangeListener to the EditText that would
  hide the EditText, show the TextView with EditText's text and then find that TodoItem,
  update its description using setDescription and notify the adapter of the change in value.

No crackers, Gromit! We've forgotten the crackers!