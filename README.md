<p align="center">
 <img width="100px" src="https://res.cloudinary.com/anuraghazra/image/upload/v1594908242/logo_ccswme.svg" align="center" alt="GitHub Readme Stats" />
 <h2 align="center">UnitTest cơ bản với JUnit</h2>
</p>

Kiểm thử đơn vị rất quan trọng vì các nhà phát triển phần mềm đôi khi cố gắng tiết kiệm thời gian thực hiện kiểm thử đơn vị tối thiểu và điều này là sai lầm vì kiểm thử đơn vị không phù hợp dẫn đến chi phí cao Sửa lỗi trong quá trình Kiểm tra hệ thống , Kiểm tra tích hợp và thậm chí là Kiểm thử Beta sau khi ứng dụng được xây dựng. Nếu thử nghiệm đơn vị thích hợp được thực hiện trong quá trình phát triển sớm, thì cuối cùng nó sẽ tiết kiệm thời gian và tiền bạc.

<div align="center">
 <table >
  <theader>
  <th>
   Nội Dung 
   </th>
   </theader>
  <tbody>
  <td>
   1: Kiểm thử đơn vị là gì?</br>
   2: Thiết kế unit test?</br>
   3: Thiết lập dự án
   </td>
   
   </tbody>
   </table>
</div>

# Kiểm thử đơn vị là gì?

- là một loại kiểm thử phần mềm trong đó các đơn vị hoặc thành phần riêng lẻ của phần mềm được kiểm tra. Mục đích là để xác nhận rằng mỗi đơn vị của mã phần mềm hoạt động như mong đợi. Kiểm thử đơn vị được thực hiện trong quá trình phát triển (giai đoạn mã hóa) của một ứng dụng bởi các nhà phát triển. Unit Test tách một phần mã và xác minh tính đúng đắn của nó. Một đơn vị có thể là một chức năng, phương pháp, thủ tục, mô-đun hoặc đối tượng riêng lẻ.

# Thiết kế unit test

> 1.Thiết lập các điều kiện cần thiết bao gồm khởi tạo các đối tượng, xác định tài nguyên cần thiết, xây dựng các dữ liệu giả.\
> 2.Gọi các phương thức cần kiểm tra.\
> 3.Kiểm tra sự hoạt động đúng đắn của các phương thức.\
> 4.Dọn dẹp tài nguyên sau khi kết thúc kiểm tra.

# Thiết lập dự án

## Maven Dependencies

```md
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>5.9.0</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
```

## Tạo lớp sinh viên

```md
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
@Entity
public class SinhVien {

    @Id
    private String id;
    private String name;
    private String email;

}

```

##Service

> service
```
public interface SinhVienService {

    List<SinhVien> getList();

    SinhVien addNew(SinhVien sinhVien);

    boolean delete(String id);

    SinhVien update(String id, SinhVien sinhVien);

    SinhVien findById(String id);
}

```

> impl
```
@AllArgsConstructor
@Service
public class SinhVienSericeImpl implements SinhVienService{

    private final SinhVienRepository sinhVienRepository;

    @Override
    public List<SinhVien> getList() {
        return sinhVienRepository.findAll();
    }

    @Override
    public SinhVien addNew(SinhVien sinhVien) {
        if(sinhVienRepository.selectExistsEmail(sinhVien.getEmail()) == 0){
            throw  new IllegalArgumentException("email da ton tai");
        }
        System.out.println(sinhVienRepository.selectExistsEmail(sinhVien.getEmail()));
        return sinhVienRepository.save(sinhVien);
    }

    @Override
    public boolean delete(String id) {
        Optional<SinhVien> sinhVien = sinhVienRepository.findById(id);
        if (sinhVien.isPresent()) {
            sinhVienRepository.deleteById(id);
            return true;
        }else{
            throw  new IllegalArgumentException("id not exists");
        }
    }

    @Override
    public SinhVien update(String id, SinhVien sinhVien) {
        SinhVien updateSinhVien = sinhVienRepository.findById(id).orElse(null);
        if(updateSinhVien == null){
            throw  new IllegalArgumentException("id not exists");
        }
        if(sinhVienRepository.selectExistsEmail(sinhVien.getEmail()) == 0){
            throw  new IllegalArgumentException("email da ton tai");
        }
        updateSinhVien.setId(sinhVien.getId());
        updateSinhVien.setName(sinhVien.getName());
        updateSinhVien.setEmail(sinhVien.getEmail());
        return sinhVienRepository.save(updateSinhVien);
    }

    @Override
    public SinhVien findById(String id) {
        return sinhVienRepository.findById(id).orElse(null);
    }
}

```

## Controller

```md
@RestController
public class sinhVienController {

    @Autowired()
    private SinhVienService sinhVienService;

    @PostMapping()
    public SinhVien addNew(@RequestBody SinhVien sinhVien){
        return sinhVienService.addNew(sinhVien);
    }

    @GetMapping("/{id}")
    public SinhVien DetailSinhVien(@PathVariable("id") String id){
        SinhVien sinhVien = null;
        try {
            sinhVien = sinhVienService.findById(id);
        }catch (Exception e){
            System.out.println(e);
        }
        return sinhVien;
    }

    @PutMapping("/{id}")
    public SinhVien update(@PathVariable("id") String id, @RequestBody SinhVien sinhVienRequest) {
        return sinhVienService.update(id, sinhVienRequest);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") String id){
        return sinhVienService.delete(id);
    }

    @GetMapping()
    public List<SinhVien> listSinhVien(){
        List<SinhVien> list = sinhVienService.getList();
        return list;
    }

}
```

## Khởi tạo lớp chứa các test case 

```
@RunWith(MockitoJUnitRunner.class)
class sinhVienControllerTest {

    @MockBean
    private SinhVienRepository sinhVienRepository;
    
     @InjectMocks
    private SinhVienController sinhVienController ;
    
    @Autowired
    private MockMvc mvc;

    @Test
    void addNew() {
    }

    @Test
    void detailSinhVien() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void listSinhVien() {
    }

    @Test
    void testAddNew() {
    }

    @Test
    void detailSinhVien() {
    }

    @Test
    void testUpdate() {
    }

    @Test
    void testDelete() {
    }

    @Test
    void testListSinhVien() {
    }
}
```

-@RunWith (SpringRunner.class) cung cấp cầu nối giữa các tính năng kiểm tra Spring Boot và JUnit. Bất cứ khi nào chúng ta sử dụng bất kỳ tính năng kiểm tra Spring Boot nào trong các bài kiểm tra JUnit , chú thích này sẽ được yêu cầu.



### Customization

You can customize the appearance of your `Stats Card` or `Repo Card` however you wish with URL params.

#### Common Options:

- `title_color` - Card's title color _(hex color)_
- `text_color` - Body text color _(hex color)_
- `icon_color` - Icons color if available _(hex color)_
- `border_color` - Card's border color _(hex color)_. (Does not apply when `hide_border` is enabled)
- `bg_color` - Card's background color _(hex color)_ **or** a gradient in the form of _angle,start,end_
- `hide_border` - Hides the card's border _(boolean)_
- `theme` - name of the theme, choose from [all available themes](./themes/README.md)
- `cache_seconds` - set the cache header manually _(min: 7200, max: 86400)_
- `locale` - set the language in the card _(e.g. cn, de, es, etc.)_
- `border_radius` - Corner rounding on the card

> Note: The minimum of cache_seconds is currently 4 hours as a temporary fix for PATs exhaustion. 
##### Gradient in bg_color

You can provide multiple comma-separated values in the bg_color option to render a gradient, with the following format:

```
&bg_color=DEG,COLOR1,COLOR2,COLOR3...COLOR10
```

> Note on cache: Repo cards have a default cache of 4 hours (14400 seconds) if the fork count & star count is less than 1k, otherwise, it's 2 hours (7200 seconds). Also, note that the cache is clamped to a minimum of 2 hours and a maximum of 24 hours.
#### Stats Card Exclusive Options:

- `hide` - Hides the [specified items](#hiding-individual-stats) from stats _(Comma-separated values)_
- `hide_title` - _(boolean)_
- `hide_rank` - _(boolean)_ hides the rank and automatically resizes the card width
- `show_icons` - _(boolean)_
- `include_all_commits` - Count total commits instead of just the current year commits _(boolean)_
- `count_private` - Count private commits _(boolean)_
- `line_height` - Sets the line-height between text _(number)_
- `custom_title` - Sets a custom title for the card
- `disable_animations` - Disables all animations in the card _(boolean)_

#### Repo Card Exclusive Options:

- `show_owner` - Show the repo's owner name _(boolean)_

#### Language Card Exclusive Options:

- `hide` - Hide the languages specified from the card _(Comma-separated values)_
- `hide_title` - _(boolean)_
- `layout` - Switch between two available layouts `default` & `compact`
- `card_width` - Set the card's width manually _(number)_
- `langs_count` - Show more languages on the card, between 1-10, defaults to 5 _(number)_
- `exclude_repo` - Exclude specified repositories _(Comma-separated values)_
- `custom_title` - Sets a custom title for the card

> :warning: **Important:**
> Language names should be uri-escaped, as specified in [Percent Encoding](https://en.wikipedia.org/wiki/Percent-encoding)
> (i.e: `c++` should become `c%2B%2B`, `jupyter notebook` should become `jupyter%20notebook`, etc.) You can use
> [urlencoder.org](https://www.urlencoder.org/) to help you do this automatically.
#### Wakatime Card Exclusive Options:

- `hide` - Hide the languages specified from the card _(Comma-separated values)_
- `hide_title` - _(boolean)_
- `line_height` - Sets the line-height between text _(number)_
- `hide_progress` - Hides the progress bar and percentage _(boolean)_
- `custom_title` - Sets a custom title for the card
- `layout` - Switch between two available layouts `default` & `compact`
- `langs_count` - Limit the number of languages on the card, defaults to all reported languages
- `api_domain` - Set a custom API domain for the card, e.g. to use services like [Hakatime](https://github.com/mujx/hakatime) or [Wakapi](https://github.com/muety/wakapi)
- `range` – Request a range different from your WakaTime default, e.g. `last_7_days`. See [WakaTime API docs](https://wakatime.com/developers#stats) for a list of available options.

---

# GitHub Extra Pins

GitHub extra pins allow you to pin more than 6 repositories in your profile using a GitHub readme profile.

Yay! You are no longer limited to 6 pinned repositories.

### Usage

Copy-paste this code into your readme and change the links.

Endpoint: `api/pin?username=anuraghazra&repo=github-readme-stats`

```md
[![Readme Card](https://github-readme-stats.vercel.app/api/pin/?username=anuraghazra&repo=github-readme-stats)](https://github.com/anuraghazra/github-readme-stats)
```

### Demo

[![Readme Card](https://github-readme-stats.vercel.app/api/pin/?username=anuraghazra&repo=github-readme-stats)](https://github.com/anuraghazra/github-readme-stats)

Use [show_owner](#customization) variable to include the repo's owner username

[![Readme Card](https://github-readme-stats.vercel.app/api/pin/?username=anuraghazra&repo=github-readme-stats&show_owner=true)](https://github.com/anuraghazra/github-readme-stats)

# Top Languages Card

The top languages card shows a GitHub user's most frequently used top language.

_NOTE: Top Languages does not indicate my skill level or anything like that; it's a GitHub metric to determine which languages have the most code on GitHub. It's a new feature of github-readme-stats._

### Usage

Copy-paste this code into your readme and change the links.

Endpoint: `api/top-langs?username=anuraghazra`

```md
[![Top Langs](https://github-readme-stats.vercel.app/api/top-langs/?username=anuraghazra)](https://github.com/anuraghazra/github-readme-stats)
```

### Exclude individual repositories

You can use `&exclude_repo=repo1,repo2` parameter to exclude individual repositories.

```md
[![Top Langs](https://github-readme-stats.vercel.app/api/top-langs/?username=anuraghazra&exclude_repo=github-readme-stats,anuraghazra.github.io)](https://github.com/anuraghazra/github-readme-stats)
```

### Hide individual languages

You can use `&hide=language1,language2` parameter to hide individual languages.

```md
[![Top Langs](https://github-readme-stats.vercel.app/api/top-langs/?username=anuraghazra&hide=javascript,html)](https://github.com/anuraghazra/github-readme-stats)
```

### Show more languages

You can use the `&langs_count=` option to increase or decrease the number of languages shown on the card. Valid values are integers between 1 and 10 (inclusive), and the default is 5.

```md
[![Top Langs](https://github-readme-stats.vercel.app/api/top-langs/?username=anuraghazra&langs_count=8)](https://github.com/anuraghazra/github-readme-stats)
```

### Compact Language Card Layout

You can use the `&layout=compact` option to change the card design.

```md
[![Top Langs](https://github-readme-stats.vercel.app/api/top-langs/?username=anuraghazra&layout=compact)](https://github.com/anuraghazra/github-readme-stats)
```

### Demo

[![Top Langs](https://github-readme-stats.vercel.app/api/top-langs/?username=anuraghazra)](https://github.com/anuraghazra/github-readme-stats)

- Compact layout

[![Top Langs](https://github-readme-stats.vercel.app/api/top-langs/?username=anuraghazra&layout=compact)](https://github.com/anuraghazra/github-readme-stats)

# Wakatime Week Stats

Change the `?username=` value to your [Wakatime](https://wakatime.com) username.

```md
[![willianrod's wakatime stats](https://github-readme-stats.vercel.app/api/wakatime?username=willianrod)](https://github.com/anuraghazra/github-readme-stats)
```

> Note: Please be aware that we currently only show data from Wakatime profiles that are public.
### Demo

[![willianrod's wakatime stats](https://github-readme-stats.vercel.app/api/wakatime?username=willianrod)](https://github.com/anuraghazra/github-readme-stats)

[![willianrod's wakatime stats](https://github-readme-stats.vercel.app/api/wakatime?username=willianrod&hide_progress=true)](https://github.com/anuraghazra/github-readme-stats)

- Compact layout

[![willianrod's wakatime stats](https://github-readme-stats.vercel.app/api/wakatime?username=willianrod&layout=compact)](https://github.com/anuraghazra/github-readme-stats)

---

### All Demos

- Default

![Anurag's GitHub stats](https://github-readme-stats.vercel.app/api?username=anuraghazra)

- Hiding specific stats

![Anurag's GitHub stats](https://github-readme-stats.vercel.app/api?username=anuraghazra&hide=contribs,issues)

- Showing icons

![Anurag's GitHub stats](https://github-readme-stats.vercel.app/api?username=anuraghazra&hide=issues&show_icons=true)

- Customize Border Color

![Anurag's GitHub stats](https://github-readme-stats.vercel.app/api?username=anuraghazra&border_color=2e4058)

- Include All Commits

![Anurag's GitHub stats](https://github-readme-stats.vercel.app/api?username=anuraghazra&include_all_commits=true)

- Themes

Choose from any of the [default themes](#themes)

![Anurag's GitHub stats](https://github-readme-stats.vercel.app/api?username=anuraghazra&show_icons=true&theme=radical)

- Gradient

![Anurag's GitHub stats](https://github-readme-stats.vercel.app/api?username=anuraghazra&bg_color=30,e96443,904e95&title_color=fff&text_color=fff)

- Customizing stats card

![Anurag's GitHub stats](https://github-readme-stats.vercel.app/api/?username=anuraghazra&show_icons=true&title_color=fff&icon_color=79ff97&text_color=9f9f9f&bg_color=151515)

- Setting card locale

![Anurag's GitHub stats](https://github-readme-stats.vercel.app/api/?username=anuraghazra&locale=es)

- Customizing repo card

![Customized Card](https://github-readme-stats.vercel.app/api/pin?username=anuraghazra&repo=github-readme-stats&title_color=fff&icon_color=f9f9f9&text_color=9f9f9f&bg_color=151515)

- Top languages

[![Top Langs](https://github-readme-stats.vercel.app/api/top-langs/?username=anuraghazra)](https://github.com/anuraghazra/github-readme-stats)

- Wakatime card

[![willianrod's wakatime stats](https://github-readme-stats.vercel.app/api/wakatime?username=willianrod)](https://github.com/anuraghazra/github-readme-stats)

---

### Quick Tip (Align The Repo Cards)

You usually won't be able to layout the images side by side. To do that you can use this approach:

```html
<a href="https://github.com/anuraghazra/github-readme-stats">
  <img align="center" src="https://github-readme-stats.vercel.app/api/pin/?username=anuraghazra&repo=github-readme-stats" />
</a>
<a href="https://github.com/anuraghazra/convoychat">
  <img align="center" src="https://github-readme-stats.vercel.app/api/pin/?username=anuraghazra&repo=convoychat" />
</a>
```

## Deploy on your own Vercel instance

#### [Check Out Step By Step Video Tutorial By @codeSTACKr](https://youtu.be/n6d4KHSKqGk?t=107)

Since the GitHub API only allows 5k requests per hour, my `https://github-readme-stats.vercel.app/api` could possibly hit the rate limiter. If you host it on your own Vercel server, then you don't have to worry about anything. Click on the deploy button to get started!

NOTE: Since [#58](https://github.com/anuraghazra/github-readme-stats/pull/58) we should be able to handle more than 5k requests and have no issues with downtime :D

[![Deploy to Vercel](https://vercel.com/button)](https://vercel.com/import/project?template=https://github.com/anuraghazra/github-readme-stats)

<details>
 <summary><b> Guide on setting up Vercel  🔨 </b></summary>

1. Go to [vercel.com](https://vercel.com/)
1. Click on `Log in`
   ![](https://files.catbox.moe/tct1wg.png)
1. Sign in with GitHub by pressing `Continue with GitHub`
   ![](https://files.catbox.moe/btd78j.jpeg)
1. Sign in to GitHub and allow access to all repositories, if prompted
1. Fork this repo
1. After forking the repo, open the [`vercel.json`](https://github.com/anuraghazra/github-readme-stats/blob/master/vercel.json#L5) file and change the `maxDuration` field to `10`
1. Go back to your [Vercel dashboard](https://vercel.com/dashboard)
1. Select `Import Project`
   ![](https://files.catbox.moe/qckos0.png)
1. Select `Import Git Repository`. Select root and keep everything as is.
   ![](https://files.catbox.moe/pqub9q.png)
1. Create a personal access token (PAT) [here](https://github.com/settings/tokens/new) and enable the `repo` permissions (this allows access to see private repo stats)
1. Add the PAT as an environment variable named `PAT_1` (as shown).
   ![](https://files.catbox.moe/0ez4g7.png)
1. Click deploy, and you're good to go. See your domains to use the API!

</details>

## :sparkling_heart: Support the project

I open-source almost everything I can, and I try to reply to everyone needing help using these projects. Obviously,
this takes time. You can use this service for free.

However, if you are using this project and are happy with it or just want to encourage me to continue creating stuff, there are a few ways you can do it:-

- Giving proper credit when you use github-readme-stats on your readme, linking back to it :D
- Starring and sharing the project :rocket:
- [![paypal.me/anuraghazra](https://ionicabizau.github.io/badges/paypal.svg)](https://www.paypal.me/anuraghazra) - You can make one-time donations via PayPal. I'll probably buy a ~~coffee~~ tea. :tea:

Thanks! :heart:

---

[![https://vercel.com?utm_source=github_readme_stats_team&utm_campaign=oss](./powered-by-vercel.svg)](https://vercel.com?utm_source=github_readme_stats_team&utm_campaign=oss)


Contributions are welcome! <3

Made with :heart: and JavaScript
