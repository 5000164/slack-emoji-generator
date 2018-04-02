package jp._5000164.text_emoji_generator.interfaces

import japgolly.scalajs.react.vdom.html_<^.{<, _}
import japgolly.scalajs.react.{BackendScope, Callback, ReactEventFromInput, StateAccessPure}
import jp._5000164.text_emoji_generator.domain._
import jp._5000164.text_emoji_generator.interfaces.Canvas.colorList
import scalacss.ScalaCssReact._

import scala.scalajs.js
import scala.util.Random

class Backend($: BackendScope[Unit, State]) {
  def render(state: State): VdomElement = {
    val f = $.zoomState(_.color)(value => _.copy(color = value))
    <.div(
      Styles.background,
      <.div(Styles.titleBar),
      <.div(
        Styles.wrapper,
        <.div(
          Styles.canvasWrapper,
          <.textarea(
            ^.id := "text",
            ^.value := state.text,
            ^.placeholder := "ここに入力",
            ^.onChange ==> onChangeText(state, f),
            Styles.text,
            if (state.fontFace == Gothic) ^.fontFamily := "Hiragino Kaku Gothic ProN" else ^.fontFamily := "Hiragino Mincho ProN",
            if (state.align == Left) ^.textAlign := "left" else ^.textAlign := "center"
          ),
          <.canvas(^.id := "canvas", Styles.canvas)
        ),
        <.div(
          Styles.saveButtonWrapper,
          <.button("保存", ^.onClick --> Canvas.save(state.text), Styles.saveButton)
        ),
        <.div(
          Styles.selectColorWrapper,
          <.input(^.value := state.color, ^.onChange ==> onChangeColor(state), Styles.textColor),
          <.button("色をランダムで選択", ^.onClick --> onClickRandomColor(state, f), Styles.randomButton)
        ),
        <.ul(
          Styles.colorList,
          Canvas.colorList.toVdomArray({
            case (key, value) => <.li(
              ^.key := key,
              ^.onClick --> onClickColor(state, value, f),
              ^.style := js.Dictionary("backgroundColor" -> s"#$value").asInstanceOf[js.Object],
              Styles.colorListItem
            )
          })
        ),
        <.div(
          Styles.fontFaceSelector,
          <.div("書体選択"),
          <.div(
            <.label(
              <.input.radio(^.name := "type-face", ^.value := "gothic", ^.checked := state.fontFace == Gothic, ^.onChange ==> onClickFontFace(state, Gothic)),
              "ゴシック体",
              Styles.fontFaceButton
            )
          ),
          <.div(
            <.label(
              <.input.radio(^.name := "type-face", ^.value := "mincho", ^.checked := state.fontFace == Mincho, ^.onChange ==> onClickFontFace(state, Mincho)),
              "明朝体",
              Styles.fontFaceButton
            )
          )
        ),
        <.div(
          Styles.alignSelector,
          <.div("位置選択"),
          <.div(
            <.label(
              <.input.radio(^.name := "align", ^.value := "left", ^.checked := state.align == Left, ^.onChange ==> onClickAlign(state, Left)),
              "左寄せ",
              Styles.alignButton
            )
          ),
          <.div(
            <.label(
              <.input.radio(^.name := "align", ^.value := "center", ^.checked := state.align == Center, ^.onChange ==> onClickAlign(state, Center)),
              "中央寄せ",
              Styles.alignButton
            )
          )
        )
      )
    )
  }

  def onChangeText(state: State, s: StateAccessPure[String])(e: ReactEventFromInput): Callback = {
    val updatedText = e.target.value
    val color = Random.shuffle(colorList).head._2

    {
      $.modState(_.copy(text = updatedText))
    } >> {
      Canvas.generate(State(updatedText, color, state.fontFace, state.align))
    } >> {
      s.setState(color)
    }
  }

  def onChangeColor(state: State)(e: ReactEventFromInput): Callback = {
    val updatedColor = e.target.value
    $.modState(_.copy(color = updatedColor))
  } >> {
    val updatedColor = e.target.value
    Canvas.generate(State(state.text, updatedColor, state.fontFace, state.align))
  }

  def onClickColor(state: State, updatedColor: String, s: StateAccessPure[String]): Callback = {
    Canvas.generate(State(state.text, updatedColor, state.fontFace, state.align))
  } >> {
    s.setState(updatedColor)
  }

  def onClickRandomColor(state: State, s: StateAccessPure[String]): Callback = {
    val color = Random.shuffle(colorList).head._2

    {
      Canvas.generate(State(state.text, color, state.fontFace, state.align))
    } >> {
      s.setState(color)
    }
  }

  def onClickFontFace(state: State, fontFace: FontFace)(e: ReactEventFromInput): Callback = {
    $.modState(_.copy(fontFace = fontFace))
  } >> {
    Canvas.generate(State(state.text, state.color, fontFace, state.align))
  }

  def onClickAlign(state: State, align: Align)(e: ReactEventFromInput): Callback = {
    $.modState(_.copy(align = align))
  } >> {
    Canvas.generate(State(state.text, state.color, state.fontFace, align))
  }
}
