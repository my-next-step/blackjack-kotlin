package dsl

import dsl.PersonBuilder.Companion.introduce
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class PersonTest {

    @Test
    fun `이름을 가진 사람을 만든다`() {
        val person: Person = introduce {
            name("김우재")
        }
        assertThat(person.name.value).isEqualTo("김우재")
    }

    @Test
    fun `이름과 회사를 가진 사람을 만든다`() {
        val person: Person = introduce {
            name("김우재")
            company("우아한형제들")
        }
        assertAll(
            { assertThat(person.name.value).isEqualTo("김우재") },
            { assertThat(person.company!!.value).isEqualTo("우아한형제들") },
        )
    }

    @Test
    fun `이름과 회사 그리고 기술을 가진 사람을 만든다`() {
        val person: Person = introduce {
            name("김우재")
            company("우아한형제들")
            skills {
                soft("A passion for problem solving")
                soft("Good communication skills")
                hard("Kotlin")
            }
        }
        assertAll(
            { assertThat(person.name.value).isEqualTo("김우재") },
            { assertThat(person.company!!.value).isEqualTo("우아한형제들") },
            {
                assertThat(person.skills.values).contains(
                    Soft("A passion for problem solving"),
                    Soft("Good communication skills"),
                    Hard("Kotlin")
                )
            },
        )
    }

    @Test
    fun `이름과 회사와 기술 그리고 언어를 가진 사람을 만든다`() {
        val person: Person = introduce {
            name("김우재")
            company("우아한형제들")
            skills {
                soft("A passion for problem solving")
                soft("Good communication skills")
                hard("Kotlin")
            }
            languages {
                "Korean" level 5
                "English" level 3
            }
        }
        assertAll(
            { assertThat(person.name.value).isEqualTo("김우재") },
            { assertThat(person.company!!.value).isEqualTo("우아한형제들") },
            {
                assertThat(person.skills.values).contains(
                    Soft("A passion for problem solving"),
                    Soft("Good communication skills"),
                    Hard("Kotlin")
                )
            },
            {
                assertThat(person.languages.languages)
                    .containsKeys(
                        Language.KOREAN,
                        Language.ENGLISH,
                    )
            }
        )
    }
}
