package denys.diomaxius.assignment_two.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import denys.diomaxius.assignment_two.data.repository.ImageRepositoryImpl
import denys.diomaxius.assignment_two.domain.repository.ImageRepository
import javax.inject.Singleton

//DI
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //Returns the repository implementation. Can be changed to another implementation
    @Provides
    @Singleton
    fun provideImageRepository(@ApplicationContext context: Context): ImageRepository =
        ImageRepositoryImpl(context)
}