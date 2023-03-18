package ru.shipa.app.presentation.list.recycler

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import ru.shipa.app.R
import ru.shipa.app.databinding.ItemFilmBinding
import ru.shipa.app.domain.model.Film
import ru.shipa.app.extension.setImageFromUrl

class FilmItem(
    private val film: Film,
    private val onClick: (Film) -> Unit
) : BindableItem<ItemFilmBinding>() {

    override fun getId(): Long = film.id.toLong()

    override fun getLayout(): Int = R.layout.item_film

    override fun initializeViewBinding(view: View) = ItemFilmBinding.bind(view)

    override fun bind(viewBinding: ItemFilmBinding, position: Int) {
        with(viewBinding) {
            image.setImageFromUrl(film.imageUrl)

            textViewTitle.text = film.title
            textViewDescription.text = film.description

            container.setOnClickListener { onClick.invoke(film) }
        }
    }
}
